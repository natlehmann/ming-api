package ar.com.marcelomingrone.vericast.reports.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import ar.com.marcelomingrone.vericast.reports.dao.PlaycountByChannelDao;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;


@Service
public class SendMailService {
	
	private static Log log = LogFactory.getLog(SendMailService.class);
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired @Qualifier(value="javaMailSender")
	protected JavaMailSender javaMailSender;
	
	@Autowired @Qualifier(value="fromAddress")
	private InternetAddress fromAddress;
	
	@Autowired
	protected MimeMessage mimeMessage;
	
	@Resource(name = "messageSource")
    private MessageSource messageSource;
	
	@Value("${base.url}")
	private String BASE_URL;
	
	@Autowired
	private PlaycountByChannelDao playcountDao;
	
	
	public void sendReport(Report report) throws MessagingException {
		
		configureMimeMessage(report, "report.ready");
		
		String text = buildMailMessage(report);
		
		List<Channel> channels = playcountDao.getChannelsForReport(report.getId());
		
		SendMailRunnable runnable = new SendMailRunnable(
				report, channels, text, mimeMessage, javaMailSender);
			
		taskExecutor.execute(runnable);
		
	}


	private String buildMailMessage(Report report) {
		
		StringBuffer approveUrl = new StringBuffer();
		approveUrl.append(BASE_URL).append("/report/byChannel/approve?user=")
			.append(report.getOwner().getUsername()).append("&id=").append(report.getId());
		
		StringBuffer rejectUrl = new StringBuffer();
		rejectUrl.append(BASE_URL).append("/report/byChannel/reject?user=")
			.append(report.getOwner().getUsername()).append("&id=").append(report.getId());
		
		return messageSource.getMessage("report.bychannel.email.body", 
				new Object[]{approveUrl.toString(), rejectUrl.toString()}, 
				new Locale(report.getOwner().getLanguage()));
		
	}


	public void sendErrorReport(Report report, Exception error) {
		
		try {
			configureMimeMessage(report, "report.error");
			
			String text = buildErrorMailMessage(report, error);
			
			SendMailErrorRunnable runnable = new SendMailErrorRunnable(
					report, text, mimeMessage, javaMailSender);
				
			taskExecutor.execute(runnable);
			
		} catch (Exception e) {
			log.error("Se produjo un error enviando el mail de informe de errores", e);
		}
		
	}
	
	
	private String buildErrorMailMessage(Report report, Exception error) throws IOException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		error.printStackTrace(new PrintStream(out));
		out.flush();
		
		String stackTrace = out.toString().replace("\n", "<br/>");
		
		return messageSource.getMessage("report.bychannel.error.email.body", 
				new String[]{stackTrace}, 
				new Locale(report.getOwner().getLanguage()));
	}


	protected void configureMimeMessage(Report report, String subjectCode) throws MessagingException {
		
		mimeMessage.setHeader("Content-Type", "text/html");
		mimeMessage.setHeader("Content-Transfer-Encoding", "base64");
		
		mimeMessage.setSubject(messageSource.getMessage(
				subjectCode, null, new Locale(report.getOwner().getLanguage())), "UTF-8");					
		mimeMessage.setSentDate(new Date());					
		mimeMessage.setFrom(fromAddress);
		
	}


}

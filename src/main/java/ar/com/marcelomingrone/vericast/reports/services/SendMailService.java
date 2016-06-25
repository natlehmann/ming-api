package ar.com.marcelomingrone.vericast.reports.services;

import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import ar.com.marcelomingrone.vericast.reports.model.Report;


@Service
public class SendMailService {
	
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
	
	
	public void sendReport(Report report) throws MessagingException {
		
		configureMimeMessage(report);
		
		String text = buildMailMessage(report);
		
		SendMailRunnable runnable = new SendMailRunnable(report, text, mimeMessage, javaMailSender);
			
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
		throw new IllegalArgumentException(error);
		
	}
	
	
	protected void configureMimeMessage(Report report) throws MessagingException {
		
		mimeMessage.setHeader("Content-Type", "text/html");
		mimeMessage.setHeader("Content-Transfer-Encoding", "base64");
		
		mimeMessage.setSubject(messageSource.getMessage(
				"report.ready", null, new Locale(report.getOwner().getLanguage())), "UTF-8");					
		mimeMessage.setSentDate(new Date());					
		mimeMessage.setFrom(fromAddress);
		
	}


}

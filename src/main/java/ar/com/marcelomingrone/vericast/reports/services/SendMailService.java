package ar.com.marcelomingrone.vericast.reports.services;

import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	
	
	public void sendReport(Report report) throws MessagingException {
		
		configureMimeMessage(report);
		
		String text = buildMailMessage(report);
		
		SendMailRunnable runnable = new SendMailRunnable(report, text, mimeMessage, javaMailSender);
			
		taskExecutor.execute(runnable);
		
	}


	private String buildMailMessage(Report report) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(messageSource.getMessage("report.bychannel.email.body", 
				new Object[]{"hola", "chau"}, new Locale(report.getOwner().getLanguage())) );
		
		return buffer.toString();
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

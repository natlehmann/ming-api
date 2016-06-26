package ar.com.marcelomingrone.vericast.reports.services;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import ar.com.marcelomingrone.vericast.reports.model.Report;

public class SendMailErrorRunnable implements Runnable {
	
	private static Log log = LogFactory.getLog(SendMailErrorRunnable.class);
	
	private MimeMessage mimeMessage;
	private JavaMailSender javaMailSender;
	private String mailBody;
	private Report report;
	
	
	public SendMailErrorRunnable(Report report, String mailBody, 
			MimeMessage mimeMessage, JavaMailSender javaMailSender) {
		
		this.report = report;
		this.mimeMessage = mimeMessage;
		this.javaMailSender = javaMailSender;
		this.mailBody = mailBody;
	}
	
	
	@Override
	public void run() {
		
		try {
			
			log.info("Enviando informe de errores a " + report.getOwner().getUsername());
			
			buildEmailMessage(mimeMessage, report.getOwner().getEmail());
			
			log.info("Enviando mail");
			javaMailSender.send(mimeMessage);
			
			log.info("Fin del proceso para " + report.getOwner().getUsername());
			
		} catch (Exception e) {
			log.error("Error al enviar mail a " + report.getOwner().getUsername(), e);
		}
		
	}


	


	public MimeMessageHelper buildEmailMessage(MimeMessage mimeMessage, String email) 
			throws MessagingException {
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);					
		
		helper.setText(mailBody, true);

		helper.setTo(new InternetAddress(email));
		
		return helper;
	}


}

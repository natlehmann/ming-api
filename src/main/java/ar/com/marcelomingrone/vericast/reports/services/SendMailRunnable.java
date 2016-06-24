package ar.com.marcelomingrone.vericast.reports.services;

import java.io.ByteArrayOutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.view.PlaycountsExcelView;
import ar.com.marcelomingrone.vericast.reports.view.ReportViewUtils;
import ar.com.marcelomingrone.vericast.reports.view.ReportViewUtils.Extension;

public class SendMailRunnable implements Runnable {
	
	private static Log log = LogFactory.getLog(SendMailRunnable.class);
	
	private PlaycountsExcelView excelView;
	
	private Report report;
	private MimeMessage mimeMessage;
	private JavaMailSender javaMailSender;
	private String mailBody;
	
	
	public SendMailRunnable(Report report, String mailBody, 
			MimeMessage mimeMessage, JavaMailSender javaMailSender) {
		
		this.report = report;
		this.mimeMessage = mimeMessage;
		this.javaMailSender = javaMailSender;
		this.mailBody = mailBody;
		this.excelView = new PlaycountsExcelView();
	}
	
	
	@Override
	public void run() {
		
		try {
			
			log.info("Enviando reporte a " + report.getOwner().getUsername());
			
			String reportName = ReportViewUtils.getReportName(report, Extension.XLS);
			
			log.info("Inicia armado del Excel");
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			excelView.buildExcelDocument(workbook, report);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			workbook.write(os);
			os.close();
			
			log.info("Configuracion del mail");					
			buildEmailMessage(mimeMessage, report.getOwner().getEmail(), 
					os.toByteArray(), reportName);
			
			log.info("Enviando mail");
			javaMailSender.send(mimeMessage);
			
			log.info("Fin del proceso para " + report.getOwner().getUsername());
			
		} catch (Exception e) {
			log.error("Error al enviar mail a " + report.getOwner().getUsername(), e);
		}
		
	}


	


	public MimeMessageHelper buildEmailMessage(MimeMessage mimeMessage,
			String email, byte[] byteArray, String reportName) 
			throws MessagingException {
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);					
		
		helper.setText(mailBody, true);

		helper.setTo(new InternetAddress(email));
		
		helper.addAttachment(reportName, new ByteArrayResource(byteArray), "application/vnd.ms-excel");
		
		return helper;
	}


}

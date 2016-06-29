package ar.com.marcelomingrone.vericast.reports.services;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

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
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.view.PlaycountsCsvView;
import ar.com.marcelomingrone.vericast.reports.view.PlaycountsExcelView;
import ar.com.marcelomingrone.vericast.reports.view.ReportViewUtils;
import ar.com.marcelomingrone.vericast.reports.view.ReportViewUtils.Extension;

public class SendMailRunnable implements Runnable {
	
	private static Log log = LogFactory.getLog(SendMailRunnable.class);
	
	private PlaycountsExcelView excelView;
	private PlaycountsCsvView csvView;
	
	private Report report;
	private List<Channel> channels;
	private MimeMessage mimeMessage;
	private JavaMailSender javaMailSender;
	private String mailBody;
	
	
	public SendMailRunnable(Report report, List<Channel> channels, String mailBody, 
			MimeMessage mimeMessage, JavaMailSender javaMailSender) {
		
		this.report = report;
		this.channels = channels;
		this.mimeMessage = mimeMessage;
		this.javaMailSender = javaMailSender;
		this.mailBody = mailBody;
		this.excelView = new PlaycountsExcelView();
		this.csvView = new PlaycountsCsvView();
	}
	
	
	@Override
	public void run() {
		
		try {
			
			log.info("Enviando reporte a " + report.getOwner().getUsername());
			
			String excelReportName = ReportViewUtils.getReportName(report, Extension.XLS);			
			ByteArrayOutputStream excelOS = buildExcelReport();
			
			String csvReportName = ReportViewUtils.getReportName(report, Extension.CSV);
			ByteArrayOutputStream csvOS = buildCsvReport();
			
			log.info("Configuracion del mail");					
			buildEmailMessage(mimeMessage, report.getOwner().getEmail(), 
					excelOS.toByteArray(), excelReportName,
					csvOS.toByteArray(), csvReportName);
			
			log.info("Enviando mail");
			javaMailSender.send(mimeMessage);
			
			log.info("Fin del proceso para " + report.getOwner().getUsername());
			
		} catch (Exception e) {
			log.error("Error al enviar mail a " + report.getOwner().getUsername(), e);
		}
		
	}


	private ByteArrayOutputStream buildCsvReport() throws IOException {
		
		log.info("Inicia armado del CSV");
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
		
		csvView.writeReport(report, channels, writer);
		
		os.close();
		return os;
	}


	private ByteArrayOutputStream buildExcelReport() throws Exception,
			IOException {
		
		log.info("Inicia armado del Excel");
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		excelView.buildExcelDocument(workbook, report);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		os.close();
		return os;
	}


	


	public MimeMessageHelper buildEmailMessage(MimeMessage mimeMessage,
			String email, byte[] excelByteArray, String excelReportName, 
			byte[] csvByteArray, String csvReportName) 
			throws MessagingException {
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);					
		
		helper.setText(mailBody, true);

		helper.setTo(new InternetAddress(email));
		
		helper.addAttachment(excelReportName, new ByteArrayResource(excelByteArray), 
				"application/vnd.ms-excel");
		
		helper.addAttachment(csvReportName, new ByteArrayResource(csvByteArray), 
				"text/csv");
		
		return helper;
	}


}

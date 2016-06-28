package ar.com.marcelomingrone.vericast.reports.view;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.view.ReportViewUtils.Extension;

public class PlaycountsExcelView extends AbstractExcelView {
	
	private static final int COLUMN_HEADER_INDEX = 5;

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PlaycountsExcelView.class);
	
	private static SimpleDateFormat headerDateFormat = new SimpleDateFormat("dd/MM/yy");
	
	private ChartStyleManager styleManager;

	
	
	
	
	public void buildExcelDocument(HSSFWorkbook workbook, Report report)
			throws Exception {
		
		this.styleManager = new ChartStyleManager(workbook);
		
		HSSFSheet excelSheet = workbook.createSheet(report.getOwner().getUsername());
		excelSheet.setDefaultColumnWidth(7);         
		setExcelHeader(excelSheet, workbook, report);
		
		setExcelRows(workbook, excelSheet, report);
	}
	

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Report report = (Report) model.get("report");
		
		String reportName = ReportViewUtils.getReportName(report, Extension.XLS);
		
        response.setHeader("Content-Disposition", "attachment; filename=\"" + reportName + "\"");
        
        buildExcelDocument(workbook, report);
		
	}
	
	
	public void setExcelHeader(HSSFSheet excelSheet, HSSFWorkbook workbook, Report report) {
		
		CellStyle style = styleManager.getColumnHeaderStyle(); 
        
		HSSFRow excelHeader = excelSheet.createRow(0);
		excelHeader.createCell(0).setCellValue("Total playcount by channel");
		excelHeader.getCell(0).setCellStyle(styleManager.getTitleCellStyle());
		excelHeader.setHeightInPoints(40);
		
		excelHeader = excelSheet.createRow(2);
		excelHeader.setHeightInPoints(22);
		excelHeader.createCell(0).setCellValue("Time period: " + report.getTimePeriod());
		excelHeader.getCell(0).setCellStyle(styleManager.getSubtitleCellStyle());
		
		excelHeader.createCell(3).setCellValue("End date: " + headerDateFormat.format(report.getEndDate()));
		excelHeader.getCell(3).setCellStyle(styleManager.getSubtitleCellStyle());
		
		
		excelHeader = excelSheet.createRow(COLUMN_HEADER_INDEX);
		excelHeader.setHeightInPoints(30);
		
		for (PlaycountsHeaderColumn header : PlaycountsHeaderColumn.values()) {
			
			excelHeader.createCell(header.ordinal()).setCellValue(header.getHeader());
			excelHeader.getCell(header.ordinal()).setCellStyle(style);
		}
	}
	


	public void setExcelRows(HSSFWorkbook workbook, HSSFSheet excelSheet, Report report){
		
		Map<String, Integer> channelHeaders = new HashMap<String, Integer>();
		
		int row = 6;
		int initialCol = PlaycountsHeaderColumn.values().length;
		
		CellStyle headerStyle = styleManager.getColumnHeaderStyle(); 
		
		for (ReportItem item : report.getItems()) {
			
			HSSFRow excelRow = excelSheet.createRow(row);
			excelRow.setHeightInPoints(18);
			
			for (PlaycountsHeaderColumn header : PlaycountsHeaderColumn.values()) {
				
				// fill in fixed fields
				excelRow.createCell(header.ordinal()).setCellValue(header.getValue(item));				
				excelRow.getCell(header.ordinal()).setCellStyle(header.getStyle(styleManager));
			}
				
			for (PlaycountByChannel pbc : item.getPlaycounts()) {
				
				Integer columnIndex = channelHeaders.get(pbc.getChannel().getName());
				int col = initialCol;
				
				if (columnIndex == null) {						
					
					// create header
					HSSFRow headerRow = excelSheet.getRow(COLUMN_HEADER_INDEX);
					headerRow.createCell(col).setCellValue(pbc.getChannel().getName());
					headerRow.getCell(col).setCellStyle(headerStyle);
					
					//update status
					channelHeaders.put(pbc.getChannel().getName(), new Integer(col));
					initialCol++;
					
				} else {
					
					col = columnIndex;
				}
				
				// create value
				excelRow.createCell(col).setCellValue(
						new HSSFRichTextString(String.valueOf(pbc.getPlaycount())));				
				excelRow.getCell(col).setCellStyle(styleManager.getRightCellStyle());
			}
			
			
			row++;
		}

		excelSheet.setColumnWidth(0, excelSheet.getColumnWidth(3) * 7);
		excelSheet.setColumnWidth(1, excelSheet.getColumnWidth(3) * 5);
		excelSheet.setColumnWidth(2, excelSheet.getColumnWidth(3) * 4);
	}


}

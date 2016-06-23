package ar.com.marcelomingrone.vericast.reports.view;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

public class ChartStyleManager {
	
	private HSSFWorkbook workbook;
	
	private CellStyle centerCellStyle = null;
	private CellStyle rightCellStyle = null;
	private CellStyle leftCellStyle = null;
	
	private CellStyle subtitleStyle;
	private CellStyle subtitleBoldStyle;
	private CellStyle titleStyle;
	private CellStyle columnHeaderStyle;
	
	public ChartStyleManager(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}
	
	
	public CellStyle getCenterCellStyle() {
		
		if (centerCellStyle == null) {
			centerCellStyle = buildCellStyle();
			centerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		}
		
		return centerCellStyle;
	}
	
	
	public CellStyle getLeftCellStyle() {
		
		if (leftCellStyle == null) {
			leftCellStyle = buildCellStyle();
			leftCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		}
		
		return leftCellStyle;
	}
	
	
	public CellStyle getRightCellStyle() {
		
		if (rightCellStyle == null) {
			rightCellStyle = buildCellStyle();
			rightCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		}
		
		return rightCellStyle;
	}
	
	
	private CellStyle buildCellStyle() {
		
		CellStyle style = workbook.createCellStyle();       
		
        Font font = workbook.createFont();       
        font.setFontName("Arial");   
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        
//        setBorders(style);
        
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
		return style;
	}
	
	
	public void setBorders(CellStyle style) {
		
		style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		
	}
	
	
	private CellStyle buildSubtitleCellStyle(HSSFWorkbook workbook, boolean bold) {
		
		CellStyle style = workbook.createCellStyle();    
		
        Font font = workbook.createFont();       
        font.setFontName("Arial");  
        font.setFontHeightInPoints((short)12);
        font.setColor(HSSFColor.GREY_80_PERCENT.index);
        
        if (bold) {
        	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        }
        
        style.setFont(font);
        
		return style;
	}
	
	
	public CellStyle getSubtitleCellStyle() {
		
		if (this.subtitleStyle == null) {
			this.subtitleStyle = buildSubtitleCellStyle(workbook, false);
		}
		
		return subtitleStyle;
	}
	
	
	public CellStyle getSubtitleBoldCellStyle() {
		
		if (this.subtitleBoldStyle == null) {
			this.subtitleBoldStyle = buildSubtitleCellStyle(workbook, true);
		}
		
		return subtitleBoldStyle;
	}
	
	
	public CellStyle getTitleCellStyle() {
		
		if (this.titleStyle == null) {
		
			titleStyle = workbook.createCellStyle();    
			
	        Font font = workbook.createFont();       
	        font.setFontName("Arial");  
	        font.setFontHeightInPoints((short)18);
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	        font.setUnderline((byte) 1);
	        titleStyle.setFont(font);
		}
        
		return titleStyle;
	}
	
	
	public CellStyle getColumnHeaderStyle() {
		
		if (this.columnHeaderStyle == null) {
		
			columnHeaderStyle = workbook.createCellStyle();    
			
	        Font font = workbook.createFont();       
	        font.setFontName("Arial");  
	        font.setFontHeightInPoints((short)12);
	        columnHeaderStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);      
	        columnHeaderStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);       
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);       
	        font.setColor(HSSFColor.WHITE.index);      
	        columnHeaderStyle.setFont(font);
	        
	        setBorders(columnHeaderStyle);
	        columnHeaderStyle.setWrapText(true);
	        
	        columnHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
		}
		
		return this.columnHeaderStyle;
	}

}

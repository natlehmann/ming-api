package ar.com.marcelomingrone.vericast.reports.view;

import java.text.SimpleDateFormat;

import ar.com.marcelomingrone.vericast.reports.model.Report;


public class ReportViewUtils {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static enum Extension {
		
		XLS {
			@Override
			public String getReportName(Report report) {
				return report.getTimePeriod() + "-" 
						+ dateFormat.format(report.getEndDate()) + ".xls";
			}
		},
		
		CSV {
			@Override
			public String getReportName(Report report) {
				return report.getTimePeriod() + "-" 
						+ dateFormat.format(report.getEndDate()) + ".csv";
			}
		};
		
		public abstract String getReportName(Report report);
	}

	public static String getReportName(Report report, Extension extension) {		
		return extension.getReportName(report);
	}
}

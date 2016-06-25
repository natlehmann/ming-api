package ar.com.marcelomingrone.vericast.reports.model;

public class NoReportException extends LocalizedException {

	private static final long serialVersionUID = 1L;
	
	public NoReportException() {
		super("error.report.does.not.exist");
	}

}

package ar.com.marcelomingrone.vericast.reports.model;

public class InvalidStateException extends LocalizedException {

	private static final long serialVersionUID = 1660907115468801046L;
	
	public InvalidStateException() {
		super("error.cannot.approve.report.in.progress");
	}

}

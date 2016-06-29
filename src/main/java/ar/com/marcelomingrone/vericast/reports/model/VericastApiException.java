package ar.com.marcelomingrone.vericast.reports.model;

public class VericastApiException extends LocalizedException {

	private static final long serialVersionUID = -1139840231950913603L;
	
	private String error;
	
	public VericastApiException(String error) {
		super("error.api.failed");
		this.error = error;
	}
	
	public String getError() {
		return error;
	}

}

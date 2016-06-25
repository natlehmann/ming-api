package ar.com.marcelomingrone.vericast.reports.model;

public class LocalizedException extends Exception {

	private static final long serialVersionUID = 16847285583581303L;
	
	private String code;

	public LocalizedException() {
	}

	public LocalizedException(String code) {
		this.code = code;
	}

	public LocalizedException(Throwable cause) {
		super(cause);
	}

	public LocalizedException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

package ar.com.marcelomingrone.vericast.reports.model;

public class DuplicateUserException extends LocalizedException {

	private static final long serialVersionUID = -8325411516113689245L;

	public DuplicateUserException(){
		super("error.duplicate.username");
	}
	
	public DuplicateUserException(String code) {
		super(code);
	}
}

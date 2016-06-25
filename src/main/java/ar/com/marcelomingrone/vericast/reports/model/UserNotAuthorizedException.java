package ar.com.marcelomingrone.vericast.reports.model;

public class UserNotAuthorizedException extends LocalizedException {

	private static final long serialVersionUID = 3673206075135590522L;
	
	public UserNotAuthorizedException() {
		super("error.user.is.not.owner");
	}

}

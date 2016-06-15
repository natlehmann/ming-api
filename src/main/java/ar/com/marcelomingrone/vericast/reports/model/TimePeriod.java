package ar.com.marcelomingrone.vericast.reports.model;


public enum TimePeriod {
	
	DAY 	("period.day"),
	WEEK	("period.week"),
	MONTH	("period.month");
	
	private String code;
	
	private TimePeriod(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}

}

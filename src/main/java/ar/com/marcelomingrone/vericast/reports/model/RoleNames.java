package ar.com.marcelomingrone.vericast.reports.model;

public enum RoleNames {

	ADMINISTRATOR,
	REPORT;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
}

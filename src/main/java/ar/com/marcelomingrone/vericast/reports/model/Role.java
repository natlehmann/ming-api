package ar.com.marcelomingrone.vericast.reports.model;

import javax.persistence.Entity;

@Entity
public class Role extends AbstractEntity {

	private static final long serialVersionUID = -6270579251811946429L;
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}

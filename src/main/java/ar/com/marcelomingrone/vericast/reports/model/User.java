package ar.com.marcelomingrone.vericast.reports.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="User", uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class User extends AbstractEntity {

	private static final long serialVersionUID = -4460933541058895296L;
	
	@NotNull @NotBlank 
	@Column(nullable=false)
	private String username;
	
	@NotNull @NotBlank
	@Column(nullable=false)
	private String password;
	
	@Column(length=2)
	private String language;
	
	private String apiKey;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	@Override
	public String toString() {
		return this.username;
	}
	
}

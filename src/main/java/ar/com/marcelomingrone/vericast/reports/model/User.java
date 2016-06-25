package ar.com.marcelomingrone.vericast.reports.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	@NotNull @NotBlank 
	@Column(nullable=false)
	private String email;
	
	@Column(length=2)
	private String language;
	
	private String apiKey;
	
	@ManyToMany
	@JoinTable(name="User_Role", joinColumns={@JoinColumn(name="user_id")}, 
		inverseJoinColumns={@JoinColumn(name="role_id")})
	private List<Role> roles;

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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		if (this.roles == null) {
			this.roles = new LinkedList<>();
		}
		this.roles.add(role);
	}
	
	@Override
	public String toString() {
		return this.username;
	}
	
}

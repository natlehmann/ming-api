package ar.com.marcelomingrone.vericast.reports.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.context.MessageSource;

@Entity
@Table(name="User", uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class User extends AbstractEntity {

	private static final long serialVersionUID = -4460933541058895296L;
	
	@NotNull @NotBlank 
	@Pattern(regexp="^[A-Za-z0-9_-]{3,30}$")
	@Column(nullable=false)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@NotNull @NotBlank @Email
	@Column(nullable=false)
	private String email;
	
	@Column(length=2)
	private String language;
	
	private String apiKey;
	
	@ManyToMany(cascade={CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name="User_Role", joinColumns={@JoinColumn(name="user_id")}, 
		inverseJoinColumns={@JoinColumn(name="role_id")})
	private List<Role> roles;
	
	public User(){}

	public User(String username) {
		this.username = username;
	}
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

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
	
	
	@Transient
	public static String getOrderingField(int index) {
		
		switch(index) {
		
		case 0:
			return "id";
		case 1:
			return "username";
		case 2:
			return "email";
		case 3:
			return "language";

		default:
			return null;
		}
	}
	
	
	@Override
	public List<String> getFieldsAsList(MessageSource msgSource, Locale locale) {
		
		List<String> fields = new LinkedList<>();
		fields.add(String.valueOf(this.getId()));
		fields.add(this.username);
		fields.add(this.email);
		fields.add(this.language);
		fields.add(isAdmin() ? msgSource.getMessage("yes", null, locale) : "<br/>");
		fields.add(super.getUpdateDeleteLinks(msgSource, locale));
		return fields;
	}
	
	@JsonIgnore
	@Transient
	public boolean isAdmin() {
		
		boolean isAdmin = false;
		if (this.roles != null) {
			for (Role role : this.roles) {
				if (role.getName().equalsIgnoreCase(RoleNames.ADMINISTRATOR.toString())) {
					isAdmin = true;
				}
			}
		}
		
		return isAdmin;
	}
}

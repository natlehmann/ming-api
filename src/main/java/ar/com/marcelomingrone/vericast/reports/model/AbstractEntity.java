package ar.com.marcelomingrone.vericast.reports.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.context.MessageSource;

@MappedSuperclass
public class AbstractEntity implements Serializable, Listable {
	
	private static final long serialVersionUID = -8982130090046767829L;
	
	protected static transient SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	protected static transient SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "(id:" + this.id + ")";
	}

	@Transient
	@JsonIgnore
	public List<String> getFieldsAsList(MessageSource msgSource, Locale locale) {
		return new LinkedList<String>();
	}

	@Transient
	@JsonIgnore
	public String getUpdateDeleteLinks(MessageSource msgSource, Locale locale) {
		return this.getUpdateLink(msgSource, locale) + this.getDeleteLink(msgSource, locale);
	}
	
	@Transient
	@JsonIgnore
	public String getUpdateLink(MessageSource msgSource, Locale locale) {
		return "<a href='update?id=" + this.id + "' class='modificar-link' title='" 
					+ msgSource.getMessage("update", null, locale) + "'></a> ";
	}
	
	@Transient
	@JsonIgnore
	public String getDeleteLink(MessageSource msgSource, Locale locale) {
		return "<a href='#' onclick='confirmarEliminar(" + this.id + ")' class='eliminar-link' title='" 
				+ msgSource.getMessage("delete", null, locale) + "'></a>";
	}
	
	
}

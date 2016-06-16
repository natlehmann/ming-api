package ar.com.marcelomingrone.vericast.reports.model.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ar.com.marcelomingrone.vericast.reports.model.AbstractEntity;

@Entity
@XmlRootElement(name="channel")
@XmlAccessorType(XmlAccessType.NONE)
public class Channel extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 4878471656970751702L;

	@Column(nullable=false)
	@XmlElement(name="name")
	private String name;
	
	@Column(nullable=false)
	@XmlElement(name="keyname")
	private String keyname;
	
	public Channel(){}
	
	public Channel(String name, String keyname) {
		this.name = name;
		this.keyname = keyname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
	
	
}

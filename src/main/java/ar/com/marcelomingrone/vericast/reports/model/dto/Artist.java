package ar.com.marcelomingrone.vericast.reports.model.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="artist")
@XmlAccessorType(XmlAccessType.NONE)
public class Artist implements Serializable {

	private static final long serialVersionUID = -7070793240945071899L;
	
	@XmlElement(name="name")
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}

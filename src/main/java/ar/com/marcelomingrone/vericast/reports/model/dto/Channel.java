package ar.com.marcelomingrone.vericast.reports.model.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="channel")
@XmlAccessorType(XmlAccessType.NONE)
public class Channel implements Serializable {

	private static final long serialVersionUID = 4878471656970751702L;

	@XmlElement(name="name")
	private String name;
	
	@XmlElement(name="keyname")
	private String keyname;

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

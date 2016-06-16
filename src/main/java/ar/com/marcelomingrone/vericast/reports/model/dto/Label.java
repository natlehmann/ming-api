package ar.com.marcelomingrone.vericast.reports.model.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="label")
@XmlAccessorType(XmlAccessType.NONE)
public class Label implements Serializable {

	private static final long serialVersionUID = 2477662211262496996L;
	
	@XmlElement(name="name")
	private String name;
	
	public Label(){}
	
	public Label(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}

package ar.com.marcelomingrone.vericast.reports.model.dto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.NONE)
public class ChannelList implements Serializable {
	
	private static final long serialVersionUID = -4872908263814472502L;

	@XmlAttribute(name="status")
	private String status;

	@XmlElementWrapper(name="listchannels")
	@XmlElement(name="channel")
	private List<Channel> channels;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public void addChannel(Channel channel) {
		if (this.channels == null) {
			this.channels = new LinkedList<>();
		}
		this.channels.add(channel);		
	}
	
}

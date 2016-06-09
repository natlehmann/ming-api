package ar.com.marcelomingrone.vericast.reports.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.NONE)
public class TrackList implements Serializable {

	private static final long serialVersionUID = 6479590591175422916L;

	@XmlAttribute(name="status")
	private String status;
	
	@XmlElementWrapper(name="toptracks")
	@XmlElement(name="track")
	private List<Track> tracks;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}
	
}

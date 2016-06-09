package ar.com.marcelomingrone.vericast.reports.model.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="track")
@XmlAccessorType(XmlAccessType.NONE)
public class Track implements Serializable {

	private static final long serialVersionUID = -7822985457699200667L;

	@XmlElement(name="name")
	private String name;
	
	@XmlElement(name="album")
	private String album;
	
	@XmlElement(name="playcount")
	private Long playcount;
	
	@XmlElement(name="artist")
	private Artist artist;
	
	@XmlElement(name="label")
	private Label label;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public Long getPlaycount() {
		return playcount;
	}

	public void setPlaycount(Long playcount) {
		this.playcount = playcount;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
	
}

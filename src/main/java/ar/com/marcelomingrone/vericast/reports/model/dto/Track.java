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
	
	@XmlElement(name="bmatid")
	private String id;
	
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
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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
		Track other = (Track) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}

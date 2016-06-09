package ar.com.marcelomingrone.vericast.reports.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class ReportItem extends AbstractEntity {

	private static final long serialVersionUID = -7785807139950711437L;

	@Column(nullable=false)
	private String trackName;
	
	@Column(nullable=false)
	private String artistName;
	
	@Column(nullable=false)
	private String labelName;
	
	@Lob @Basic(fetch=FetchType.EAGER)
	@Column(nullable=false, length=4000)
	private String playcounts;
	
	@ManyToOne(optional=false)
	private Report report;

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getPlaycounts() {
		return playcounts;
	}

	public void setPlaycounts(String playcounts) {
		this.playcounts = playcounts;
	}
	
	public Report getReport() {
		return report;
	}
	
	public void setReport(Report report) {
		this.report = report;
	}
	
}

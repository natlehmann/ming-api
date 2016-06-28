package ar.com.marcelomingrone.vericast.reports.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;

@Entity
public class ReportItem extends AbstractEntity {

	private static final long serialVersionUID = -7785807139950711437L;
	
	public ReportItem() {}

	public ReportItem(String trackName, String artistName, String labelName,
			Long totalPlayCount) {
		super();
		this.trackName = trackName;
		this.artistName = artistName;
		this.labelName = labelName;
		this.totalPlayCount = totalPlayCount;
	}



	@Column(nullable=false)
	private String trackName;
	
	@Column(nullable=false)
	private String trackId;
	
	@Column(nullable=false)
	private String artistName;
	
	@Column(nullable=false)
	private String labelName;
	
	private Long totalPlayCount;
	
	@OneToMany(mappedBy="reportItem", cascade=CascadeType.ALL)
	private List<PlaycountByChannel> playcounts;
	
	@ManyToOne(optional=false)
	private Report report;
	
	@Transient
	private transient Map<Channel, Long> playcountsByChannel;

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

	public List<PlaycountByChannel> getPlaycounts() {
		return playcounts;
	}
	
	public void setPlaycounts(List<PlaycountByChannel> playcounts) {
		this.playcounts = playcounts;
	}
	
	public Report getReport() {
		return report;
	}
	
	public void setReport(Report report) {
		this.report = report;
	}
	
	public String getTrackId() {
		return trackId;
	}
	
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	
	public Long getTotalPlayCount() {
		return totalPlayCount;
	}
	
	public void setTotalPlayCount(Long totalPlayCount) {
		this.totalPlayCount = totalPlayCount;
	}
	
	public void addPlaycount(PlaycountByChannel playcount) {
		if (this.playcounts == null) {
			this.playcounts = new LinkedList<>();
		}
		
		this.playcounts.add(playcount);
		playcount.setReportItem(this);
		
	}
	
	public static class TrackIdComparator implements Comparator<ReportItem> {

		@Override
		public int compare(ReportItem o1, ReportItem o2) {
			return o1.getTrackId().compareTo(o2.getTrackId());
		}
		
	}
	
}

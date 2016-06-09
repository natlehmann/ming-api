package ar.com.marcelomingrone.vericast.reports.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Report extends AbstractEntity {

	private static final long serialVersionUID = -6583423007874315353L;

	@ManyToOne(optional=false)
	private User owner;
	
	@Column(length=20)
	private String timePeriod;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@OneToMany(mappedBy="report", cascade=CascadeType.ALL)
	private List<ReportItem> items;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public List<ReportItem> getItems() {
		return items;
	}
	
	public void setItems(List<ReportItem> items) {
		this.items = items;
	}
	
}

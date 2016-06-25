package ar.com.marcelomingrone.vericast.reports.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Report extends AbstractEntity {

	private static final long serialVersionUID = -6583423007874315353L;
	
	public static enum State {
		IN_PROCESS,
		FINISHED,
		APPROVED;
	}

	@ManyToOne(optional=false)
	private User owner;
	
	@Column(length=20)
	private String timePeriod;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@OneToMany(mappedBy="report", cascade=CascadeType.ALL)
	private List<ReportItem> items;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private State state;

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
		
		if (this.items != null) {
			for (ReportItem item : this.items) {
				item.setReport(this);
			}
		}
	}

	public void addItem(ReportItem item) {
		if (this.items == null) {
			this.items = new LinkedList<>();
		}
		
		this.items.add(item);
		item.setReport(this);
		
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
}

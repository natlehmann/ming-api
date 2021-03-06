package ar.com.marcelomingrone.vericast.reports.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.context.MessageSource;

import ar.com.marcelomingrone.vericast.reports.controllers.Utils;

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

	@Transient
	public static String getOrderingField(int index) {
		
		boolean isAdmin = Utils.isCurrentUserAdministrator();
		
		switch(index) {
		case 0:
			return "id";
			
		case 1:
			if (isAdmin) {
				return "owner.username";
			
			} else {
				return "timePeriod";
			}
			
		case 2:
			if (isAdmin) {
				return "timePeriod";
				
			} else {
				return "endDate";
			}
			
		case 3:
			if (isAdmin) {
				return "endDate";
			
			} else {
				return "state";
			}
			
		case 4:
			return "state";
		}
		
		return null;
	}
	
	@Transient
	@Override
	public List<String> getFieldsAsList(MessageSource msgSource, Locale locale) {
		
		List<String> fields = new LinkedList<>();
		fields.add(String.valueOf(this.getId()));
		
		if (Utils.isCurrentUserAdministrator()) {
			fields.add(this.owner.getUsername());
		}
		
		fields.add(msgSource.getMessage(this.timePeriod, null, locale));
		fields.add(format.format(endDate));
		fields.add(msgSource.getMessage(this.state.toString(), null, locale));
		
		switch(this.state) {
		
		case APPROVED:
			fields.add(getDownloadExcelLink(msgSource, locale) + getDownloadCsvLink(msgSource, locale) 
					+ getDeleteLink(msgSource, locale));
			break;
			
		case FINISHED:
			fields.add(getApproveLink(msgSource, locale) + " " + getDeleteLink(msgSource, locale));
			break;
			
		default:
			fields.add("<br/>");
				
		}
		
		return fields;
		
	}

	@Transient
	@JsonIgnore
	private String getDownloadExcelLink(MessageSource msgSource, Locale locale) {
		return "<a href='downloadExcel?id=" + this.getId() + "' class='excel-link' title='" 
				+ msgSource.getMessage("download.excel.report", null, locale) + "'></a> ";
	}
	
	@Transient
	@JsonIgnore
	private String getDownloadCsvLink(MessageSource msgSource, Locale locale) {
		return "<a href='downloadCsv?id=" + this.getId() + "' class='csv-link' title='" 
				+ msgSource.getMessage("download.csv.report", null, locale) + "'></a> ";
	}

	@Transient
	@JsonIgnore
	private String getApproveLink(MessageSource msgSource, Locale locale) {		
		return "<a href='approve?id=" + this.getId() + "&user=" + this.getOwner().getUsername() 
				+ "' class='aprobar-link' title='" 
				+ msgSource.getMessage("approve", null, locale) + "'></a> ";
	}
	
	@Transient
	@JsonIgnore
	public String getDeleteLink(MessageSource msgSource, Locale locale) {
		return "<a href='#' onclick='confirmarEliminarReporte(" + this.getId() 
				+ ",\"" + this.owner.getUsername() + "\")' class='eliminar-link' title='" 
				+ msgSource.getMessage("delete", null, locale) + "'></a>";
	}
	
}

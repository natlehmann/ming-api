package ar.com.marcelomingrone.vericast.reports.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;

@Entity
public class PlaycountByChannel extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1081973436836716680L;

	@ManyToOne(optional=false, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private Channel channel;
	
	private long playcount;
	
	@ManyToOne(optional=false)
	private ReportItem reportItem;
	
	public PlaycountByChannel(){}
	
	public PlaycountByChannel(Channel channel, long playcount) {
		this.channel = channel;
		this.playcount = playcount;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public long getPlaycount() {
		return playcount;
	}

	public void setPlaycount(long playcount) {
		this.playcount = playcount;
	}
	
	public ReportItem getReportItem() {
		return reportItem;
	}
	
	public void setReportItem(ReportItem reportItem) {
		this.reportItem = reportItem;
	}

}

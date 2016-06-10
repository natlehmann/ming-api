package ar.com.marcelomingrone.vericast.reports.model.dto;

import java.io.Serializable;

public class PlaycountByChannel implements Serializable {
	
	private static final long serialVersionUID = 1081973436836716680L;

	private Channel channel;
	
	private long playcount;
	
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
	
	

}

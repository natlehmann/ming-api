package ar.com.marcelomingrone.vericast.reports.dao;

import org.hibernate.Session;

import ar.com.marcelomingrone.vericast.reports.model.AbstractEntity;
import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.Artist;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;
import ar.com.marcelomingrone.vericast.reports.model.dto.Label;
import ar.com.marcelomingrone.vericast.reports.model.dto.Track;
import ar.com.marcelomingrone.vericast.reports.model.dto.TrackList;

public class TestDataBuilder {
	
	private Session currentSession;
	
	public TestDataBuilder(Session session) {
		this.currentSession = session;
	}
	
	public User buildUser(String username, String apiKey) {
		
		User user = new User();
		user.setUsername(username);
		user.setApiKey(apiKey);
		user.setPassword("password");
		currentSession.saveOrUpdate(user);
		
		return user;
	}
	
	public User buildUser(String username) {
		
		return buildUser(username, null);
	}

	public ChannelList buildChannelList(int elementCount) {
		
		ChannelList channelList = new ChannelList();
		
		for (int i = 0; i < elementCount; i++) {
			Channel channel = new Channel();
			channel.setName("channel " + i);
			channel.setKeyname("keyname " + i);
			channelList.addChannel(channel);
		}
		
		return channelList;
	}
	
	public Track buildTrack(String id, long playcount) {
		
		Track track = new Track();
		track.setAlbum("album");
		track.setId(id);
		track.setName("name");
		track.setPlaycount(playcount);
		track.setArtist(new Artist("artist"));
		track.setLabel(new Label("label"));
		return track;
	}

	public TrackList buildTrackList(int elementCount) {
		
		TrackList trackList = new TrackList();
		
		for (int i = 0; i < elementCount; i++) {
			Track track = new Track();
			track.setAlbum("album");
			track.setId("id" + i);
			track.setName("name");
			track.setPlaycount((long) i);
			trackList.addTrack(track);
		}
		
		return trackList;
	}

	public Report buildReport(User user) {
		
		Report report = new Report();
		report.setOwner(user);
		currentSession.saveOrUpdate(report);
		
		return report;
	}

	public ReportItem buildReportItem(Report report, String bmaitId) {
		
		ReportItem item = new ReportItem();
		item.setTrackId(bmaitId);
		item.setArtistName("artistName");
		item.setTrackName("trackName");
		item.setLabelName("labelName");
		
		report.addItem(item);
		
		currentSession.saveOrUpdate(item);
		
		return item;
	}

	public Channel buildChannel(String name, String keyname) {
		
		Channel channel = new Channel();
		channel.setKeyname(keyname);
		channel.setName(name);
		currentSession.saveOrUpdate(channel);
		
		return channel;
	}

	public PlaycountByChannel buildPlaycountByChannel(ReportItem item, String channelName) {
		
		PlaycountByChannel playcount = new PlaycountByChannel();
		playcount.setChannel(buildChannel(channelName, channelName));
		playcount.setPlaycount(3);
		item.addPlaycount(playcount);
		
		currentSession.saveOrUpdate(playcount);
		
		return playcount;
	}
	
	public AbstractEntity save(AbstractEntity entity) {
		currentSession.saveOrUpdate(entity);
		return entity;
	}

}

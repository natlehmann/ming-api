package ar.com.marcelomingrone.vericast.reports.dao;

import java.util.Date;

import org.hibernate.Session;

import ar.com.marcelomingrone.vericast.reports.model.AbstractEntity;
import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.Report.State;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.Role;
import ar.com.marcelomingrone.vericast.reports.model.RoleNames;
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
		user.setEmail("email");
		user.setLanguage("es");
		user.setPassword("password");
		currentSession.saveOrUpdate(user);
		
		return user;
	}
	
	public User buildUser(String username) {		
		return buildUser(username, null);
	}
	
	public User buildAdminUser(String username) {
		User user = buildUser(username);
		Role role = buildRole(RoleNames.ADMINISTRATOR.toString());
		user.addRole(role);
		currentSession.saveOrUpdate(user);
		return user;
	}

	public Role buildRole(String name) {
		Role role = new Role();
		role.setName(name);
		currentSession.saveOrUpdate(role);
		return role;
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

	public Report buildReport(User user, State state, String timePeriod, Date endDate) {
		
		Report report = new Report();
		report.setOwner(user);
		report.setState(state);
		report.setTimePeriod(timePeriod);
		report.setEndDate(endDate);
		currentSession.saveOrUpdate(report);
		
		return report;
	}
	
	public Report buildReport(User user, State state) {
		return buildReport(user, state, null, null);
	}
	
	public Report buildReport(User user) {
		return buildReport(user, State.IN_PROCESS, null, null);
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
		return buildPlaycountByChannel(item, buildChannel(channelName, channelName));
	}

	public PlaycountByChannel buildPlaycountByChannel(ReportItem item, Channel channel) {
		
		PlaycountByChannel playcount = new PlaycountByChannel();
		playcount.setChannel(channel);
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

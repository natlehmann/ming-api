package ar.com.marcelomingrone.vericast.reports.dao;

import org.hibernate.Session;

import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;

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

}

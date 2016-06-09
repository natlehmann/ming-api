package ar.com.marcelomingrone.vericast.reports.dao;

import org.hibernate.Session;

import ar.com.marcelomingrone.vericast.reports.model.User;

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

}

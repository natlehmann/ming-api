package ar.com.marcelomingrone.vericast.reports.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.marcelomingrone.vericast.reports.dao.UserDao;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;

@Service
public class ReportService {
	
	@Value(value="${vericast.api.channel.list}")
	private String CHANNEL_LIST_URL;
	
	private enum ApiParams {
		
		USER,
		API,
		LIMIT;
		
		public String toString() {
			return this.name().toLowerCase();
		}
		
		public String asParam() {
			return this.toString() + "=";
		}
	}
	
	@Autowired
	private UserDao userDao;
	

	public Report getPlaycountsByChannel(String timePeriod, Date endDate) {
		
        ChannelList channelList = getChannelList();
		
		return new Report();
	}
	
	protected ChannelList getChannelList() {
		
		User currentUser = userDao.getCurrentUser();
		
		RestTemplate restTemplate = new RestTemplate();
        ChannelList channelList = restTemplate.getForObject(
        		CHANNEL_LIST_URL + "?" + ApiParams.USER.asParam() + currentUser.getUsername() + "&" 
        				+ ApiParams.API.asParam() + currentUser.getApiKey() + "&" 
        				+ ApiParams.LIMIT.asParam() + "2000", 
        		ChannelList.class);
        
        return channelList;
	}

}

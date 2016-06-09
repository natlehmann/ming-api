package ar.com.marcelomingrone.vericast.reports.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.marcelomingrone.vericast.reports.dao.UserDao;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;
import ar.com.marcelomingrone.vericast.reports.model.dto.TrackList;

@Service
public class ReportService {
	
	private static final String PAGE_LIMIT = "2000";
	
	@Value(value="${vericast.api.channel.list}")
	private String CHANNEL_LIST_URL;
	
	@Value(value="${vericast.api.track.list.by.channel}")
	private String TRACKS_BY_CHANNEL_URL;
	
	private enum ApiParams {
		
		USER,
		API,
		LIMIT,
		CHANNEL,
		END,
		PERIOD;
		
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
		
		User currentUser = userDao.getCurrentUser();
		
        ChannelList channelList = getChannelList(currentUser);
        
        for (Channel channel : channelList.getChannels()) {
        	
        	TrackList trackList = getTrackListByChannel(channel.getKeyname(), currentUser);
        }
		
		return new Report();
	}
	
	protected TrackList getTrackListByChannel(String keyname, User currentUser) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(TRACKS_BY_CHANNEL_URL)
				.append("?").append(ApiParams.USER.asParam()).append(currentUser.getUsername())
				.append("&").append(ApiParams.API.asParam()).append(currentUser.getApiKey())
				.append("&").append(ApiParams.LIMIT.asParam()).append(PAGE_LIMIT)
				.append("&").append(ApiParams.CHANNEL.asParam()).append(keyname)
				.append("&").append(ApiParams.END.asParam()).append("20160606")
				.append("&").append(ApiParams.PERIOD.asParam()).append("week");
		
		RestTemplate restTemplate = new RestTemplate();
		TrackList list = restTemplate.getForObject(buffer.toString(), TrackList.class);
        
        return list;
	}

	protected ChannelList getChannelList(User currentUser) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(CHANNEL_LIST_URL)
				.append("?").append(ApiParams.USER.asParam()).append(currentUser.getUsername())
				.append("&").append(ApiParams.API.asParam()).append(currentUser.getApiKey())
				.append("&").append(ApiParams.LIMIT.asParam()).append(PAGE_LIMIT);
		
		RestTemplate restTemplate = new RestTemplate();
        ChannelList channelList = restTemplate.getForObject(buffer.toString(), ChannelList.class);
        
        return channelList;
	}

}

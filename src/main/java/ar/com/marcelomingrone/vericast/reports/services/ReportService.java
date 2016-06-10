package ar.com.marcelomingrone.vericast.reports.services;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.marcelomingrone.vericast.reports.controllers.ReportByChannelController;
import ar.com.marcelomingrone.vericast.reports.dao.UserDao;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;
import ar.com.marcelomingrone.vericast.reports.model.dto.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.dto.Track;
import ar.com.marcelomingrone.vericast.reports.model.dto.TrackList;

@Service
public class ReportService {
	
	private static Log log = LogFactory.getLog(ReportService.class);
	
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
		
		Map<Track, List<PlaycountByChannel>> playcountsByTrack = new HashMap<>();
		
        ChannelList channelList = getChannelList(currentUser);
        log.info("Recuperando lista de canales " + channelList);
        
        for (Channel channel : channelList.getChannels()) {
        	
        	log.info("Recuperando lista de track para el canal " + channel.getName());
        	TrackList trackList = getTrackListByChannel(channel.getKeyname(), currentUser);
        	
        	for (Track track : trackList.getTracks()) {
        		
        		List<PlaycountByChannel> playcounts = playcountsByTrack.get(track);
        		if (playcounts == null) {
        			playcounts = new LinkedList<>();
        			playcountsByTrack.put(track, playcounts);
        		}
        		
        		playcounts.add(new PlaycountByChannel(channel, track.getPlaycount()));
        	}
        }
        
        
        List<ReportItem> items = new LinkedList<>();
        
        
        for (Track track : playcountsByTrack.keySet()) {
        	
        	ReportItem item = new ReportItem();
        	item.setArtistName(track.getArtist() != null ? track.getArtist().getName() : null);
        	item.setLabelName(track.getLabel() != null ? track.getLabel().getName() : null);
        	item.setTrackName(track.getName());
        	
        	item.setIndividualPlaycounts(playcountsByTrack.get(track));
        	
        	items.add(item);
        }
		
        
        Report report = new Report();
        report.setEndDate(new Date()); // TODO
        report.setOwner(currentUser);
        report.setTimePeriod(timePeriod);
        report.setItems(items);
        
		return report;
	}
	
	protected TrackList getTrackListByChannel(String keyname, User currentUser) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(TRACKS_BY_CHANNEL_URL)
				.append("?").append(ApiParams.USER.asParam()).append(currentUser.getUsername())
				.append("&").append(ApiParams.API.asParam()).append(currentUser.getApiKey())
				.append("&").append(ApiParams.LIMIT.asParam()).append(PAGE_LIMIT)
				.append("&").append(ApiParams.CHANNEL.asParam()).append(keyname)
				.append("&").append(ApiParams.END.asParam()).append("20160606")  //TODO!!!!!!!!
				.append("&").append(ApiParams.PERIOD.asParam()).append("week");  // TODO!!!!!!!!!!!
		
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

package ar.com.marcelomingrone.vericast.reports.services;

import java.text.SimpleDateFormat;
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

import ar.com.marcelomingrone.vericast.reports.dao.ReportDao;
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
	
	private static final SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyyMMdd");
	
	
	@Autowired
	private VericastApiDelegate api;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ReportDao reportDao;
	
/*
	public void buildPlaycountsByChannel(Report report, String timePeriod, Date endDate) {
		
		Map<Track, List<PlaycountByChannel>> playcountsByTrack = new HashMap<>();
		
        List<Channel> channelList = api.getChannelList(report.getOwner());
        log.info("Recuperando lista de canales para usuario " + report.getOwner());
        
        for (Channel channel : channelList) {
        	
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
        report.setEndDate(endDate);
        report.setOwner(currentUser);
        report.setTimePeriod(timePeriod);
        report.setItems(items);
        
		return report;
	}
	
	protected TrackList getTrackListByChannel(String keyname, User currentUser, 
			Date endDate, String timePeriod) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(TRACKS_BY_CHANNEL_URL)
				.append("?").append(ApiParams.USER.asParam()).append(currentUser.getUsername())
				.append("&").append(ApiParams.API.asParam()).append(currentUser.getApiKey())
				.append("&").append(ApiParams.LIMIT.asParam()).append(PAGE_LIMIT)
				.append("&").append(ApiParams.CHANNEL.asParam()).append(keyname)
				.append("&").append(ApiParams.END.asParam()).append(endDateFormat.format(endDate))  
				.append("&").append(ApiParams.PERIOD.asParam()).append(timePeriod); 
		
		RestTemplate restTemplate = new RestTemplate();
		TrackList list = restTemplate.getForObject(buffer.toString(), TrackList.class);
        
        return list;
	}

	*/

	public Report buildReport(String timePeriod, Date endDate) {
		
		User currentUser = userDao.getCurrentUser();
		
		Report report = new Report();
        report.setEndDate(endDate);
        report.setOwner(currentUser);
        report.setTimePeriod(timePeriod);
        
        report = reportDao.save(report);
        return report;
	}
	
	

}

package ar.com.marcelomingrone.vericast.reports.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.VericastApiException;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;
import ar.com.marcelomingrone.vericast.reports.model.dto.Track;
import ar.com.marcelomingrone.vericast.reports.model.dto.TrackList;

@Service
public class VericastApiDelegate {
	
	private static Log log = LogFactory.getLog(VericastApiDelegate.class);
	
	public static final SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyyMMdd");
	
	@Value(value="${vericast.api.channel.list}")
	protected String CHANNEL_LIST_URL;
	
	@Value(value="${vericast.api.track.list.by.channel}")
	protected String TRACKS_BY_CHANNEL_URL;
	
	private static final int PAGE_LIMIT = 2000;

	private static final String OK = "ok";
	
	private enum ApiParams {
		
		USER,
		API,
		LIMIT,
		CHANNEL,
		END,
		PERIOD,
		PAGE;
		
		public String toString() {
			return this.name().toLowerCase();
		}
		
		public String asParam() {
			return this.toString() + "=";
		}
	}
	
	public List<Channel> getChannelList(User currentUser) throws VericastApiException {
		return getChannelList(currentUser, new RestTemplate());
	}
	
	protected List<Channel> getChannelList(User currentUser, RestTemplate restTemplate) 
			throws VericastApiException {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(CHANNEL_LIST_URL)
				.append("?").append(ApiParams.USER.asParam()).append(currentUser.getUsername())
				.append("&").append(ApiParams.API.asParam()).append(currentUser.getApiKey())
				.append("&").append(ApiParams.LIMIT.asParam()).append(PAGE_LIMIT);
		
		String baseUrl = buffer.toString();
		int page = 1;
		List<Channel> channels = new LinkedList<>();
		
		log.debug("Buscando lista de canales PAGINA 1 para usuario " + currentUser);
		ChannelList channelList = requestChannels(restTemplate, buffer.toString());
		
		while (channelList.getChannels() != null && !channelList.getChannels().isEmpty()) {
			
			channels.addAll(channelList.getChannels());
			page++;
			
			buffer = new StringBuffer(baseUrl);
			buffer.append("&").append(ApiParams.PAGE.asParam()).append(page);
			
			// solo se recupera siguiente pagina si la cant de registros fue igual al limite
			if (channelList.getChannels().size() == PAGE_LIMIT) {
				
				log.debug("Buscando lista de canales PAGINA " + page + " para usuario " + currentUser);
				channelList = requestChannels(restTemplate, buffer.toString());
				
			} else {
				channelList.setChannels(null);
			}
			
		}
        
        return channels;
	}

	protected ChannelList requestChannels(RestTemplate restTemplate,
			String query) throws VericastApiException {
		
		ChannelList channelList = restTemplate.getForObject(query, ChannelList.class);
		
		if (!channelList.getStatus().equalsIgnoreCase(OK)) {
			throw new VericastApiException(channelList.getError());
		}
		
		return channelList;
	}
	
	
	public List<Track> getTracksByChannel(String keyname, User currentUser, 
			Date endDate, String timePeriod) throws VericastApiException {
		return getTracksByChannel(keyname, currentUser, endDate, timePeriod, new RestTemplate());
	}
	
	protected List<Track> getTracksByChannel(String keyname, User currentUser, 
			Date endDate, String timePeriod, RestTemplate restTemplate) throws VericastApiException {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(TRACKS_BY_CHANNEL_URL)
				.append("?").append(ApiParams.USER.asParam()).append(currentUser.getUsername())
				.append("&").append(ApiParams.API.asParam()).append(currentUser.getApiKey())
				.append("&").append(ApiParams.LIMIT.asParam()).append(PAGE_LIMIT)
				.append("&").append(ApiParams.CHANNEL.asParam()).append(keyname)
				.append("&").append(ApiParams.END.asParam()).append(endDateFormat.format(endDate))  
				.append("&").append(ApiParams.PERIOD.asParam()).append(timePeriod.toLowerCase()); 
		
		
		String baseUrl = buffer.toString();
		int page = 1;
		List<Track> tracks = new LinkedList<>();
		
		log.debug("Buscando lista de tracks PAGINA 1 para usuario " + currentUser +  " para canal " + keyname);
		TrackList list = requestTracks(restTemplate, buffer.toString());
		
		while (list.getTracks() != null && !list.getTracks().isEmpty()) {
			
			tracks.addAll(list.getTracks());
			page++;
			
			buffer = new StringBuffer(baseUrl);
			buffer.append("&").append(ApiParams.PAGE.asParam()).append(page);
			
			// solo se recupera siguiente pagina si la cant de registros fue igual al limite
			if (list.getTracks().size() == PAGE_LIMIT) {
				
				log.debug("Buscando lista de tracks PAGINA " + page + " para usuario " + currentUser 
						+  " para canal " + keyname);
				list = requestTracks(restTemplate, buffer.toString());
				
			} else {
				list.setTracks(null);
			}
			
		}
        
        return tracks;
	}

	protected TrackList requestTracks(RestTemplate restTemplate,
			String query) throws VericastApiException {
		
		TrackList list = restTemplate.getForObject(query, TrackList.class);
		
		if (!list.getStatus().equalsIgnoreCase(OK)) {
			throw new VericastApiException(list.getError());
		}
		
		return list;
	}

}

package ar.com.marcelomingrone.vericast.reports.services;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;

@Service
public class VericastApiDelegate {
	
	private static Log log = LogFactory.getLog(VericastApiDelegate.class);
	
	@Value(value="${vericast.api.channel.list}")
	protected String CHANNEL_LIST_URL;
	
	@Value(value="${vericast.api.track.list.by.channel}")
	protected String TRACKS_BY_CHANNEL_URL;
	
	private static final int PAGE_LIMIT = 2000;
	
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
	
	public List<Channel> getChannelList(User currentUser) {
		return getChannelList(currentUser, new RestTemplate());
	}
	
	public List<Channel> getChannelList(User currentUser, RestTemplate restTemplate) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(CHANNEL_LIST_URL)
				.append("?").append(ApiParams.USER.asParam()).append(currentUser.getUsername())
				.append("&").append(ApiParams.API.asParam()).append(currentUser.getApiKey())
				.append("&").append(ApiParams.LIMIT.asParam()).append(PAGE_LIMIT);
		
		String baseUrl = buffer.toString();
		int page = 1;
		List<Channel> channels = new LinkedList<>();
		
		log.info("Buscando lista de canales PAGINA 1 para usuario " + currentUser);
		ChannelList channelList = restTemplate.getForObject(buffer.toString(), ChannelList.class);
		
		while (channelList.getChannels() != null && !channelList.getChannels().isEmpty()) {
			
			channels.addAll(channelList.getChannels());
			page++;
			
			buffer = new StringBuffer(baseUrl);
			buffer.append("&").append(ApiParams.PAGE.asParam()).append(page);
			
			// solo se recupera siguiente pagina si la cant de registros fue igual al limite
			if (channelList.getChannels().size() == PAGE_LIMIT) {
				
				log.info("Buscando lista de canales PAGINA " + page + " para usuario " + currentUser);
				channelList = restTemplate.getForObject(buffer.toString(), ChannelList.class);
				
			} else {
				channelList.setChannels(null);
			}
			
		}
        
        return channels;
	}

}

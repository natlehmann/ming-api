package ar.com.marcelomingrone.vericast.reports.services;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;

@Service
public class ReportService {

	public Report getPlaycountsByChannel(String timePeriod, Date endDate) {
		
        ChannelList channelList = getChannelList();
		
		return new Report();
	}
	
	protected ChannelList getChannelList() {
		
		RestTemplate restTemplate = new RestTemplate();
        ChannelList channelList = restTemplate.getForObject(
        		"http://api.vericast.bmat.me/1/channel/list?user=capif&api=5ed325681f&limit=2000", 
        		ChannelList.class);
        
        return channelList;
	}

}

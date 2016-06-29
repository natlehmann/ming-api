package ar.com.marcelomingrone.vericast.reports.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.TimePeriod;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.VericastApiException;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;
import ar.com.marcelomingrone.vericast.reports.model.dto.Track;
import ar.com.marcelomingrone.vericast.reports.model.dto.TrackList;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class VericastApiDelegateTest extends AbstractTest {
	
	@Autowired
	private VericastApiDelegate api;
	
	private User user;
	
	@Before
	public void initTest() {
		
		user = builder.buildUser(USERNAME, API_KEY);
		mockPrincipal(USERNAME);
	}

	@Test
	public void getChannelListNotNull() throws VericastApiException {
		
		List<Channel> list = api.getChannelList(user);
		assertNotNull(list);
		assertFalse(list.isEmpty());
		
		for (Channel channel : list) {
			assertNotNull(channel.getKeyname());
			assertNotNull(channel.getName());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getChannelListOnePage() throws VericastApiException {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		String expectedUrl = api.CHANNEL_LIST_URL + "?user=" + USERNAME + "&api=" + API_KEY + "&limit=2000";
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildChannelList(300));
		
		List<Channel> result = api.getChannelList(user, restTemplate);
				
		assertEquals(300, result.size());
		Mockito.verify(restTemplate, Mockito.times(1)).getForObject(expectedUrl, ChannelList.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getTrackListOnePage() {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		String expectedUrl = api.TRACKS_BY_CHANNEL_URL + "?user=" + USERNAME + "&api=" + API_KEY 
				+ "&limit=2000&channel=keyname&end=" 
				+ VericastApiDelegate.endDateFormat.format(new Date()) + "&period=week";
		
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildTrackList(300));
		
		List<Track> result = api.getTracksByChannel("keyname", user, new Date(), 
				TimePeriod.WEEK.toString(), restTemplate);
				
		assertEquals(300, result.size());
		Mockito.verify(restTemplate, Mockito.times(1)).getForObject(expectedUrl, TrackList.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getChannelListSecondPageEmpty() throws VericastApiException {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		String expectedUrl = api.CHANNEL_LIST_URL + "?user=" + USERNAME + "&api=" + API_KEY + "&limit=2000";
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildChannelList(2000)).thenReturn(builder.buildChannelList(0));
		
		List<Channel> result = api.getChannelList(user, restTemplate);
				
		assertEquals(2000, result.size());
		Mockito.verify(restTemplate).getForObject(expectedUrl, ChannelList.class);
		Mockito.verify(restTemplate).getForObject(expectedUrl + "&page=2", ChannelList.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getTrackListSecondPageEmpty() {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		String expectedUrl = api.TRACKS_BY_CHANNEL_URL + "?user=" + USERNAME + "&api=" + API_KEY 
				+ "&limit=2000&channel=keyname&end=" 
				+ VericastApiDelegate.endDateFormat.format(new Date()) + "&period=week";
		
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildTrackList(2000)).thenReturn(builder.buildTrackList(0));
		
		List<Track> result = api.getTracksByChannel("keyname", user, new Date(), 
				TimePeriod.WEEK.toString(), restTemplate);
				
		assertEquals(2000, result.size());
		Mockito.verify(restTemplate).getForObject(expectedUrl, TrackList.class);
		Mockito.verify(restTemplate).getForObject(expectedUrl + "&page=2", TrackList.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getChannelListTwoPages() throws VericastApiException {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		String expectedUrl = api.CHANNEL_LIST_URL + "?user=" + USERNAME + "&api=" + API_KEY + "&limit=2000";
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildChannelList(2000))
			.thenReturn(builder.buildChannelList(500));
		
		List<Channel> result = api.getChannelList(user, restTemplate);
				
		assertEquals(2500, result.size());
		Mockito.verify(restTemplate).getForObject(expectedUrl, ChannelList.class);
		Mockito.verify(restTemplate).getForObject(expectedUrl + "&page=2", ChannelList.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getTrackListTwoPages() {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		String expectedUrl = api.TRACKS_BY_CHANNEL_URL + "?user=" + USERNAME + "&api=" + API_KEY 
				+ "&limit=2000&channel=keyname&end=" 
				+ VericastApiDelegate.endDateFormat.format(new Date()) + "&period=week";
		
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildTrackList(2000))
			.thenReturn(builder.buildTrackList(500));
		
		List<Track> result = api.getTracksByChannel("keyname", user, new Date(), 
				TimePeriod.WEEK.toString(), restTemplate);
				
		assertEquals(2500, result.size());
		Mockito.verify(restTemplate).getForObject(expectedUrl, TrackList.class);
		Mockito.verify(restTemplate).getForObject(expectedUrl + "&page=2", TrackList.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getChannelListThreePages() throws VericastApiException {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		String expectedUrl = api.CHANNEL_LIST_URL + "?user=" + USERNAME + "&api=" + API_KEY + "&limit=2000";
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildChannelList(2000))
			.thenReturn(builder.buildChannelList(2000))
			.thenReturn(builder.buildChannelList(500));
		
		List<Channel> result = api.getChannelList(user, restTemplate);
				
		assertEquals(4500, result.size());
		Mockito.verify(restTemplate).getForObject(expectedUrl, ChannelList.class);
		Mockito.verify(restTemplate).getForObject(expectedUrl + "&page=2", ChannelList.class);
		Mockito.verify(restTemplate).getForObject(expectedUrl + "&page=3", ChannelList.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=VericastApiException.class)
	public void getChannelListWithError() throws VericastApiException {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildChannelListWithError());
		
		api.getChannelList(user, restTemplate);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getTrackListThreePages() {
		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		
		String expectedUrl = api.TRACKS_BY_CHANNEL_URL + "?user=" + USERNAME + "&api=" + API_KEY 
				+ "&limit=2000&channel=keyname&end=" 
				+ VericastApiDelegate.endDateFormat.format(new Date()) + "&period=week";
		
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(builder.buildTrackList(2000))
			.thenReturn(builder.buildTrackList(2000))
			.thenReturn(builder.buildTrackList(500));
		
		List<Track> result = api.getTracksByChannel("keyname", user, new Date(), 
				TimePeriod.WEEK.toString(), restTemplate);
				
		assertEquals(4500, result.size());
		Mockito.verify(restTemplate).getForObject(expectedUrl, TrackList.class);
		Mockito.verify(restTemplate).getForObject(expectedUrl + "&page=2", TrackList.class);
		Mockito.verify(restTemplate).getForObject(expectedUrl + "&page=3", TrackList.class);
	}
	
	
	@Test
	public void getTrackListByChannelNotNull() throws ParseException {
		
		List<Track> list = api.getTracksByChannel("rockandpop--------------------01", 
				user, VericastApiDelegate.endDateFormat.parse("20160606"), 
				TimePeriod.WEEK.toString());
		assertNotNull(list);
		assertFalse(list.isEmpty());
		
		for (Track track : list) {
			
			assertNotNull(track.getName());
			assertNotNull("BmatID nulo para el track " + track, track.getId());
			assertNotNull("Artista nulo para el track " + track, track.getArtist().getName());
			assertNotNull("Label nulo para el track " + track, track.getLabel().getName());
			assertNotNull("Playcount nulo para el track " + track, track.getPlaycount());
			assertTrue("Playcount negativo para el track " + track, track.getPlaycount() >= 0);
		}
	}

}

package ar.com.marcelomingrone.vericast.reports.services;

import static org.junit.Assert.*;

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
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;

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
	public void getChannelListNotNull() {
		
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
	public void getChannelListOnePage() {
		
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
	public void getChannelListSecondPageEmpty() {
		
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
	public void getChannelListTwoPages() {
		
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
	public void getChannelListThreePages() {
		
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

}
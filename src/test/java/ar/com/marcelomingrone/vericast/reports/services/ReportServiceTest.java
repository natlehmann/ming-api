package ar.com.marcelomingrone.vericast.reports.services;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mvc-dispatcher-servlet-test.xml"})
@Transactional
public class ReportServiceTest {
	
	@Autowired
	private ReportService service;

	@Test
	public void getChannelListNotNull() {
		
		ChannelList list = service.getChannelList();
		assertNotNull(list);
		assertFalse(list.getChannels().isEmpty());
		
		for (Channel channel : list.getChannels()) {
			assertNotNull(channel.getKeyname());
			assertNotNull(channel.getName());
		}
		
	}

}

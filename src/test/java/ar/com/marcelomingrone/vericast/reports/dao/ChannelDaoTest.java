package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;

public class ChannelDaoTest extends AbstractTest {
	
	@Autowired
	private ChannelDao dao;

	@Test
	public void getByKeynameNull() {
		Channel channel = dao.getByKeyname("keyname");
		assertNull(channel);
	}
	
	@Test
	public void getByKeynameNotNull() {
		
		Channel expected = builder.buildChannel("name", "keyname");
		Channel channel = dao.getByKeyname("keyname");
		assertEquals(expected, channel);
	}
	
	@Test
	public void findOrSaveNotFound() {
		
		Channel newChannel = new Channel("name", "keyname");
		Channel result = dao.findOrSave(newChannel);
		assertNotNull(result.getId());
		assertEquals("keyname", result.getKeyname());
	}
	
	@Test
	public void findOrSaveFound() {
		
		Channel existingChannel = builder.buildChannel("name", "keyname");
		Channel newChannel = new Channel("name", "keyname");		
		
		Channel result = dao.findOrSave(newChannel);
		assertEquals(existingChannel.getId(), result.getId());
		assertEquals(newChannel.getKeyname(), result.getKeyname());
	}

}

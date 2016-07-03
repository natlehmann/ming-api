package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.*;

import java.util.List;

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
	
	@Test
	public void getAllFilteredNoResult() {
		
		List<Channel> result = dao.getAllPaginatedAndFiltered(0, 100, null, null, null);
		assertTrue(result.isEmpty());
		assertEquals(0, dao.getCount());
	}
	
	@Test
	public void getAllFilteredNoFilter() {
		
		Channel channel1 = builder.buildChannel("NameA", "keyname");
		Channel channel2 = builder.buildChannel("ABC", "keyname");
		
		List<Channel> result = dao.getAllPaginatedAndFiltered(0, 100, "name", "ASC", null);
		
		assertEquals(2, result.size());
		assertEquals(channel2, result.get(0));
		assertEquals(channel1, result.get(1));
		
		assertEquals(2, dao.getCount());
	}
	
	@Test
	public void getAllFilteredWithFilter() {
		
		builder.buildChannel("NameA", "keyname");
		Channel channel2 = builder.buildChannel("ABC", "keyname");
		
		List<Channel> result = dao.getAllPaginatedAndFiltered(0, 100, "name", "ASC", "B");
		
		assertEquals(1, result.size());
		assertEquals(channel2, result.get(0));
		
		assertEquals(1, dao.getCount("B"));
	}
	
	@Test
	public void getAllFilteredWithFilter2() {
		
		Channel channel1 = builder.buildChannel("NameA", "keyname");
		Channel channel2 = builder.buildChannel("ABC", "keyname");
		
		List<Channel> result = dao.getAllPaginatedAndFiltered(0, 100, "name", "DESC", "A");
		
		assertEquals(2, result.size());
		assertEquals(channel1, result.get(0));
		assertEquals(channel2, result.get(1));
		
		assertEquals(2, dao.getCount("A"));
	}

}

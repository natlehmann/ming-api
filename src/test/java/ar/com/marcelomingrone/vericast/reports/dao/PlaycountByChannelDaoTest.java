package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;

public class PlaycountByChannelDaoTest extends AbstractTest {
	
	@Autowired
	private PlaycountByChannelDao dao;
	
	private ReportItem item;
	
	@Before
	public void initTest() {
		Report report = builder.buildReport(builder.buildUser(USERNAME));
		item = builder.buildReportItem(report, "bmaitId");
	}

	@Test
	public void getByReportItemNoResults() {
		
		List<PlaycountByChannel> result = dao.getByReportItem(item);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void getByReportItemWithResults() {
		
		PlaycountByChannel playcount1 = builder.buildPlaycountByChannel(item, "channel1");
		PlaycountByChannel playcount2 = builder.buildPlaycountByChannel(item, "channel2");
		
		List<PlaycountByChannel> result = dao.getByReportItem(item);
		assertEquals(2, result.size());
		assertTrue(result.contains(playcount1));
		assertTrue(result.contains(playcount2));
	}
	
	@Test
	public void getChannelsForReportNoResult() {
		
		List<Channel> result = dao.getChannelsForReport(1L);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void getChannelsForReportOneResult() {
		
		Channel channel1 = builder.buildChannel("one", "keyname1");
		Channel channel2 = builder.buildChannel("two", "keyname2");
		Channel channel3 = builder.buildChannel("three", "keyname3");
		
		PlaycountByChannel playcount1 = builder.buildPlaycountByChannel(item, channel1);
		builder.buildPlaycountByChannel(item, channel2);
		builder.buildPlaycountByChannel(item, channel3);
		
		ReportItem item2 = builder.buildReportItem(item.getReport(), "bmaitId2");
		
		builder.buildPlaycountByChannel(item2, channel2);
		builder.buildPlaycountByChannel(item2, channel1);
		
		List<Channel> result = dao.getChannelsForReport(playcount1.getReportItem().getReport().getId());
		
		assertEquals(3, result.size());
		assertTrue(result.contains(channel1));
		assertTrue(result.contains(channel2));
		assertTrue(result.contains(channel3));
	}
	
	@Test
	public void getChannelsForReportMoreThanOneResult() {
		
		PlaycountByChannel playcount1 = builder.buildPlaycountByChannel(item, "channel1");
		
		List<Channel> result = dao.getChannelsForReport(playcount1.getReportItem().getReport().getId());
		assertEquals(1, result.size());
		assertEquals(playcount1.getChannel(), result.get(0));
	}

	
	@Test
	public void getAllFilteredNoResult() {
		
		List<PlaycountByChannel> result = dao.getAllPaginatedAndFiltered(0, 100, null, null, null);
		assertTrue(result.isEmpty());
		assertEquals(0, dao.getCount());
	}
	
	@Test
	public void getAllFilteredNoFilter() {
		
		PlaycountByChannel play1 = builder.buildPlaycountByChannel(item, "channelName1");
		PlaycountByChannel play2 = builder.buildPlaycountByChannel(item, "channelName2");
		
		List<PlaycountByChannel> result = dao.getAllPaginatedAndFiltered(0, 100, "channel.name", "DESC", null);
		
		assertEquals(2, result.size());
		assertEquals(play2, result.get(0));
		assertEquals(play1, result.get(1));
		
		assertEquals(2, dao.getCount());
	}
	
	@Test
	public void getAllFilteredWithFilter() {
		
		builder.buildPlaycountByChannel(item, "channelName1");
		PlaycountByChannel play2 = builder.buildPlaycountByChannel(item, "channelName2");
		
		List<PlaycountByChannel> result = dao.getAllPaginatedAndFiltered(0, 100, "channel.name", "ASC", "2");
		
		assertEquals(1, result.size());
		assertEquals(play2, result.get(0));
		
		assertEquals(1, dao.getCount("2"));
	}
	
	@Test
	public void getAllFilteredWithFilter2() {
		
		PlaycountByChannel play1 = builder.buildPlaycountByChannel(item, "channelName1");
		PlaycountByChannel play2 = builder.buildPlaycountByChannel(item, "channelName2");
		
		List<PlaycountByChannel> result = dao.getAllPaginatedAndFiltered(
				0, 100, "channel.name", "DESC", "annel");
		
		assertEquals(2, result.size());
		assertEquals(play2, result.get(0));
		assertEquals(play1, result.get(1));
		
		assertEquals(2, dao.getCount("annel"));
	}
}

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

}

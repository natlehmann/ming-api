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

}

package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.User;

public class ReportDaoTest extends AbstractTest {
	
	@Autowired
	private ReportDao dao;
	
	private User user;
	private Report report;
	private ReportItem item1;
	private ReportItem item2;
	private ReportItem item3;
	
	@Before
	public void initTest() {
		
		user = builder.buildUser(USERNAME);
		report = builder.buildReport(user);
		item1 = builder.buildReportItem(report, "bmaitId1");
		item2 = builder.buildReportItem(report, "bmaitId2");
		item3 = builder.buildReportItem(report, "bmaitId3");
		builder.buildPlaycountByChannel(item1, "channelName");
		builder.buildPlaycountByChannel(item2, "channelName");
		builder.buildPlaycountByChannel(item3, "channelName");
	}

	@Test
	public void getByIdWithItemsAndPlaycountsNotLazy() {
		Report result = dao.getByIdWithItemsAndPlaycounts(report.getId());
		assertEquals(3, result.getItems().size());
		assertEquals(1, result.getItems().get(0).getPlaycounts().size());
	}
	
	@Test
	public void getByIdWithItemsAndPlaycountsNull() {
		Report result = dao.getByIdWithItemsAndPlaycounts(10110L);
		assertNull(result);
	}
	
	@Test
	public void getByIdWithItemsOrderedByPlaycounts() {
		
		item1.setTotalPlayCount(5L);
		builder.save(item1);
		item2.setTotalPlayCount(2L);
		builder.save(item2);
		item3.setTotalPlayCount(8L);
		builder.save(item3);
		
		Report result = dao.getByIdWithItemsAndPlaycounts(report.getId());
		assertEquals(3, result.getItems().size());
		assertEquals(item3, result.getItems().get(0));
		assertEquals(item1, result.getItems().get(1));
		assertEquals(item2, result.getItems().get(2));
	}
	
	
	@Test
	public void getByIdWithItemsSamePlaycount() {
		
		item1.setTotalPlayCount(2L);
		item1.setTrackName("tercero");
		builder.save(item1);
		
		item2.setTotalPlayCount(2L);
		item2.setTrackName("segundo");
		builder.save(item2);
		
		item3.setTotalPlayCount(2L);
		item3.setTrackName("primero");
		builder.save(item3);
		
		Report result = dao.getByIdWithItemsAndPlaycounts(report.getId());
		assertEquals(3, result.getItems().size());
		assertEquals(item3, result.getItems().get(0));
		assertEquals(item2, result.getItems().get(1));
		assertEquals(item1, result.getItems().get(2));
	}

}

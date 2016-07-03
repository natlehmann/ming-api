package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.Report.State;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.TimePeriod;
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
	
	@Test
	public void getReportsForCurrentUserNoResults() {
		
		User newUser = builder.buildUser("other");
		List<Report> result = dao.getReportsForCurrentUser(newUser, 0, 100, null, null, null);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void getReportsForCurrentUserNoFilter() {
		
		List<Report> result = dao.getReportsForCurrentUser(user, 0, 100, null, null, null);
		assertEquals(1, result.size());
		assertEquals(report, result.get(0));
	}
	
	@Test
	public void getReportsForCurrentUserWithFilter() {
		
		report.setState(State.APPROVED);
		builder.save(report);
		
		List<Report> result = dao.getReportsForCurrentUser(
				user, 0, 100, State.APPROVED.toString(), null, null);
		assertEquals(1, result.size());
		assertEquals(report, result.get(0));
		
		result = dao.getReportsForCurrentUser(
				user, 0, 100, State.FINISHED.toString(), null, null);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void getReportsForCurrentUserCountZero() {
		
		User newUser = builder.buildUser("other");
		long count = dao.getReportsForCurrentUserCount(newUser, null);
		assertEquals(0, count);
	}
	
	@Test
	public void getReportsForCurrentUserCountNoFilter() {
		
		long count = dao.getReportsForCurrentUserCount(user, null);
		assertEquals(1, count);
	}
	
	@Test
	public void getReportsForCurrentUserCountWithFilter() {
		
		report.setState(State.APPROVED);
		builder.save(report);
		
		long count = dao.getReportsForCurrentUserCount(user, State.APPROVED.toString());
		assertEquals(1, count);
		
		count = dao.getReportsForCurrentUserCount(user, State.FINISHED.toString());
		
		assertEquals(0, count);
	}
	
	
	@Test
	public void getAllFilteredOneResult() {
		
		List<Report> result = dao.getAllPaginatedAndFiltered(0, 100, null, null, null);
		assertEquals(1, result.size());
		assertEquals(1, dao.getCount());
	}
	
	@Test
	public void getAllFilteredNoFilter() {
		
		Report report2 = builder.buildReport(user, State.FINISHED, TimePeriod.MONTH.toString(), new Date());
		
		List<Report> result = dao.getAllPaginatedAndFiltered(0, 100, "state", "ASC", null);
		
		assertEquals(2, result.size());
		assertEquals(report2, result.get(0));
		assertEquals(report, result.get(1));
		
		assertEquals(2, dao.getCount());
	}
	
	@Test
	public void getAllFilteredWithFilter() {
		
		Report report2 = builder.buildReport(user, State.FINISHED, TimePeriod.MONTH.toString(), new Date());
		
		List<Report> result = dao.getAllPaginatedAndFiltered(0, 100, "state", "ASC", "mo");
		
		assertEquals(1, result.size());
		assertEquals(report2, result.get(0));
		
		assertEquals(1, dao.getCount("mo"));
	}
	
	@Test
	public void getAllFilteredWithFilter2() {
		
		report.setTimePeriod(TimePeriod.DAY.toString());
		builder.save(report);
		Report report2 = builder.buildReport(user, State.APPROVED, TimePeriod.DAY.toString(), new Date());
		
		List<Report> result = dao.getAllPaginatedAndFiltered(0, 100, "state", "DESC", "a");
		
		assertEquals(2, result.size());
		assertEquals(report, result.get(0));
		assertEquals(report2, result.get(1));
		
		assertEquals(2, dao.getCount("a"));
	}

}

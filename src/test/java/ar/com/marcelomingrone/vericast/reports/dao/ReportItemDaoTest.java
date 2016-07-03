package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.User;

public class ReportItemDaoTest extends AbstractTest {
	
	@Autowired
	private ReportItemDao dao;
	
	private Report report;
	private User user;
	
	@Before
	public void initTest() {
		user = builder.buildUser(USERNAME);
		report = builder.buildReport(user);
	}

	@Test
	public void getByTrackIdNull() {
		ReportItem item = dao.getByTrackId(report, "bmatId");
		assertNull(item);
	}
	
	@Test
	public void getByTrackIdNotNull() {
		ReportItem expected = builder.buildReportItem(report, "bmatId");
		ReportItem item = dao.getByTrackId(report, "bmatId");
		assertEquals(expected, item);
	}
	
	
	@Test
	public void getAllFilteredNoResult() {
		
		List<ReportItem> result = dao.getAllPaginatedAndFiltered(0, 100, null, null, null);
		assertTrue(result.isEmpty());
		assertEquals(0, dao.getCount());
	}
	
	@Test
	public void getAllFilteredNoFilter() {
		
		ReportItem item1 = builder.buildReportItem(report, "bmatid1");
		ReportItem item2 = builder.buildReportItem(report, "bmatid2");
		
		List<ReportItem> result = dao.getAllPaginatedAndFiltered(0, 100, "trackId", "DESC", null);
		
		assertEquals(2, result.size());
		assertEquals(item2, result.get(0));
		assertEquals(item1, result.get(1));
		
		assertEquals(2, dao.getCount());
	}
	
	@Test
	public void getAllFilteredWithFilter() {
		
		builder.buildReportItem(report, "bmatid1", "trackOne", "artistOne");
		ReportItem item2 = builder.buildReportItem(report, "bmatid2", "trackTwo", "artistTwo");
		
		List<ReportItem> result = dao.getAllPaginatedAndFiltered(0, 100, "trackName", "ASC", "Tw");
		
		assertEquals(1, result.size());
		assertEquals(item2, result.get(0));
		
		assertEquals(1, dao.getCount("Tw"));
	}
	
	@Test
	public void getAllFilteredWithFilter2() {
		
		ReportItem item1 = builder.buildReportItem(report, "bmatid1", "trackOne", "artistOne");
		ReportItem item2 = builder.buildReportItem(report, "bmatid2", "trackTwo", "artistTwo");
		
		List<ReportItem> result = dao.getAllPaginatedAndFiltered(0, 100, "artistName", "DESC", "tis");
		
		assertEquals(2, result.size());
		assertEquals(item2, result.get(0));
		assertEquals(item1, result.get(1));
		
		assertEquals(2, dao.getCount("tis"));
	}
	

}

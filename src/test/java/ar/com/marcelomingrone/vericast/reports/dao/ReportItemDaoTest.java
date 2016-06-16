package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.*;

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

}

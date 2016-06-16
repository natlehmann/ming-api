package ar.com.marcelomingrone.vericast.reports.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReportTest {

	@Test
	public void addItemSetsReport() {
		
		ReportItem item = new ReportItem();
		Report report = new Report();
		
		report.addItem(item);
		assertEquals(1, report.getItems().size());
		assertSame(report, item.getReport());
	}

}

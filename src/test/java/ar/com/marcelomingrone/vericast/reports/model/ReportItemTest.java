package ar.com.marcelomingrone.vericast.reports.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReportItemTest {

	@Test
	public void addPlaycountSetsItem() {
		
		PlaycountByChannel playcount = new PlaycountByChannel();
		ReportItem item = new ReportItem();
		
		item.addPlaycount(playcount);
		
		assertEquals(1, item.getPlaycounts().size());
		assertSame(item, playcount.getReportItem());
	}

}

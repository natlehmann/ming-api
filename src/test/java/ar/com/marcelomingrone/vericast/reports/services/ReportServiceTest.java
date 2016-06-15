package ar.com.marcelomingrone.vericast.reports.services;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ReportServiceTest extends AbstractTest {
	
	@Autowired
	private ReportService service;

	
	@Test
	public void borrar() {
		assertTrue(true);
	}
	
	

}

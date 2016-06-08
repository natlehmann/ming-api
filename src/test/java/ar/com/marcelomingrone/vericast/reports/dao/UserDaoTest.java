package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.*;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mvc-dispatcher-servlet-test.xml"})
@Transactional
public class UserDaoTest {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Test
	public void test() {
		assertNotNull(sessionFactory);
	}

}

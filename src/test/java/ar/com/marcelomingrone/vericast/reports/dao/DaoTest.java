package ar.com.marcelomingrone.vericast.reports.dao;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mvc-dispatcher-servlet-test.xml"})
@Transactional
public abstract class DaoTest {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected TestDataBuilder builder;	

	@Before
	public void init() {
		builder = new TestDataBuilder(sessionFactory.getCurrentSession());
	}
}

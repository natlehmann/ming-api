package ar.com.marcelomingrone.vericast.reports;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.dao.TestDataBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mvc-dispatcher-servlet-test.xml"})
@Transactional
public abstract class AbstractTest {
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	@Value(value="${test.username}")
	protected String USERNAME;
	
	@Value(value="${test.apiKey}")
	protected String API_KEY;
	
	protected TestDataBuilder builder;	

	@Before
	public void init() {
		builder = new TestDataBuilder(sessionFactory.getCurrentSession());
	}
	
	protected void mockPrincipal(String username) {
		
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getName()).thenReturn(username);
		
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		
		SecurityContextHolder.setContext(securityContext);
	}
	
	@After
	public void cleanUp() {
		
		SecurityContextHolder.clearContext();
	}
}

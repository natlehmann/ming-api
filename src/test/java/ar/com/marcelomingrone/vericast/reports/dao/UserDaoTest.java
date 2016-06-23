package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.User;

public class UserDaoTest extends AbstractTest {
	
	@Autowired
	private UserDao dao;

	@Test
	public void getByUsernameNoResults() {
		User user = dao.getByUsername("username");
		assertNull(user);
	}
	
	@Test
	public void getByUsernameWithResult() {
		User user = builder.buildUser("username");
		User result = dao.getByUsername("username");
		assertEquals(result, user);
	}
	
	@Test
	public void getByUsernameCaseInsensitive() {
		User user = new User();
		user.setPassword("password");
		user.setUsername("username");
		user.setEmail("email");
		user = dao.save(user);
		
		User result = dao.getByUsername("USERNAME");
		assertEquals(result, user);
	}
	
	@Test
	public void getByUsernameCaseInsensitive2() {
		User user = new User();
		user.setPassword("password");
		user.setUsername("userNAME");
		user.setEmail("email");
		user = dao.save(user);
		
		User result = dao.getByUsername("Username");
		assertEquals(result, user);
	}
	
	@Test
	public void findCurrentUser() {
		
		User user = builder.buildUser("username");
		mockPrincipal("username");
		
		User result = dao.getCurrentUser();
		assertEquals(result, user);
	}
	
	@Test
	public void currentUserNotFound() {
		
		mockPrincipal("username");
		User result = dao.getCurrentUser();
		assertNull(result);
	}
	
	@Test
	public void noCurrentUser() {
		
		builder.buildUser("username");
		
		User result = dao.getCurrentUser();
		assertNull(result);
	}

}

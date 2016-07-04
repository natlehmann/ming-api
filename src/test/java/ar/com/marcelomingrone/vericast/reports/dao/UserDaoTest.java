package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.Role;
import ar.com.marcelomingrone.vericast.reports.model.RoleNames;
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
	public void getByEmailCaseInsensitive() {
		User user = new User();
		user.setPassword("password");
		user.setUsername("username");
		user.setEmail("email");
		user = dao.save(user);
		
		User result = dao.getByEmail("EMAIL");
		assertEquals(result, user);
	}
	
	@Test
	public void getByEmailNoResult() {
		assertNull(dao.getByEmail("EMAIL"));
	}
	
	@Test
	public void getByUsernameNoResult() {
		assertNull(dao.getByUsername("userName"));
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
	public void getByEmailCaseInsensitive2() {
		User user = new User();
		user.setPassword("password");
		user.setUsername("userNAME");
		user.setEmail("EMAIL");
		user = dao.save(user);
		
		User result = dao.getByEmail("Email");
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
	
	
	@Test
	public void getAllFilteredNoResult() {
		
		List<User> result = dao.getAllPaginatedAndFiltered(0, 100, null, null, null);
		assertTrue(result.isEmpty());
		assertEquals(0, dao.getCount());
	}
	
	@Test
	public void getAllFilteredNoFilter() {
		
		User user1 = builder.buildUser("username1");
		User user2 = builder.buildUser("username2");
		
		List<User> result = dao.getAllPaginatedAndFiltered(0, 100, "username", "DESC", null);
		
		assertEquals(2, result.size());
		assertEquals(user2, result.get(0));
		assertEquals(user1, result.get(1));
		
		assertEquals(2, dao.getCount());
	}
	
	@Test
	public void getAllFilteredWithFilter() {
		
		builder.buildUser("username1");
		User user2 = builder.buildUser("username2");
		
		List<User> result = dao.getAllPaginatedAndFiltered(0, 100, "username", "ASC", "2");
		
		assertEquals(1, result.size());
		assertEquals(user2, result.get(0));
		
		assertEquals(1, dao.getCount("2"));
	}
	
	@Test
	public void getAllFilteredWithFilter2() {
		
		User user1 = builder.buildUser("username1");
		User user2 = builder.buildUser("username2");
		
		List<User> result = dao.getAllPaginatedAndFiltered(0, 100, "username", "DESC", "ser");
		
		assertEquals(2, result.size());
		assertEquals(user2, result.get(0));
		assertEquals(user1, result.get(1));
		
		assertEquals(2, dao.getCount("ser"));
	}

	
	@Test
	public void getAllPaginatedAndFilteredNonLazyRoles() {
		
		Role adminRole = builder.buildRole(RoleNames.ADMINISTRATOR.toString());
		Role userRole = builder.buildRole(RoleNames.REPORT.toString());
		
		User user1 = new User("username1", "email1", "pass");
		user1.addRole(userRole);
		user1 = dao.save(user1);
		
		User user2 = new User("username2", "email2", "pass");
		user2.addRole(userRole);
		user2.addRole(adminRole);
		user2 = dao.save(user2);
		
		List<User> result = dao.getAllPaginatedAndFiltered(0, 100, "username", "DESC", "ser");
		
		assertEquals(user2, result.get(0));
		assertEquals(2, user2.getRoles().size());
		
		assertEquals(user1, result.get(1));
		assertEquals(1, user1.getRoles().size());
		
	}
	

}

package ar.com.marcelomingrone.vericast.reports.services;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.dao.RoleDao;
import ar.com.marcelomingrone.vericast.reports.dao.UserDao;
import ar.com.marcelomingrone.vericast.reports.model.DuplicateUserException;
import ar.com.marcelomingrone.vericast.reports.model.LocalizedException;
import ar.com.marcelomingrone.vericast.reports.model.Role;
import ar.com.marcelomingrone.vericast.reports.model.RoleNames;
import ar.com.marcelomingrone.vericast.reports.model.User;

public class UserServiceTest extends AbstractTest {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private UserDao dao;
	
	@Autowired
	private RoleDao roleDao;
	
	private Role adminRole;
	private Role userRole;
	
	@Before
	public void initTest() {
		adminRole = builder.buildRole(RoleNames.ADMINISTRATOR.toString());
		userRole = builder.buildRole(RoleNames.REPORT.toString());
	}

	@Test(expected=DuplicateUserException.class)
	public void saveUserWithDuplicateUsername() throws LocalizedException {
		builder.buildUser("username");
		service.save(new User("username"), false);
	}
	
	@Test(expected=DuplicateUserException.class)
	public void saveUserWithDuplicateUsernameCaseInsensitive() throws LocalizedException {
		dao.save(new User("USERNAME", "email", "pass"));
		service.save(new User("username", "emailll", "pass"), false);
	}
	
	@Test(expected=DuplicateUserException.class)
	public void saveUserWithDuplicateUsernameCaseInsensitive2() throws LocalizedException {
		dao.save(new User("username", "email", "pass"));
		service.save(new User("USERname", "emailll", "pass"), false);
	}
	
	@Test(expected=DuplicateUserException.class)
	public void saveUserWithDuplicateEmail() throws LocalizedException {
		dao.save(new User("username", "email", "pass"));
		service.save(new User("other", "email", "pass"), false);
	}
	
	@Test(expected=DuplicateUserException.class)
	public void saveUserWithDuplicateEmailCaseInsensitive() throws LocalizedException {
		dao.save(new User("username", "EMAIL", "pass"));
		service.save(new User("other", "email", "pass"), false);
	}
	
	@Test(expected=DuplicateUserException.class)
	public void saveUserWithDuplicateEmailCaseInsensitive2() throws LocalizedException {
		dao.save(new User("username", "email", "pass"));
		service.save(new User("other", "EMAIL", "pass"), false);
	}
	
	@Test
	public void saveNewNormalUser() throws LocalizedException {
		
		User user = new User("username", "email", "pass");
		user = service.save(user, false);
		
		assertNotNull(user.getId());
		User result = service.getById(user.getId());
		List<Role> roles = roleDao.getForUser(result);
		
		assertEquals(1, roles.size());
		assertEquals(userRole, roles.get(0));
	}
	
	@Test
	public void saveNewAdminUser() throws LocalizedException {
		
		User user = new User("username", "email", "pass");
		user = service.save(user, true);
		
		assertNotNull(user.getId());
		User result = service.getById(user.getId());
		List<Role> roles = roleDao.getForUser(result);
		
		assertEquals(2, roles.size());
		assertTrue(roles.contains(userRole));
		assertTrue(roles.contains(adminRole));
	}
	
	@Test
	public void updateNormalUser() throws LocalizedException {
		
		//before starting
		User existent = dao.save(new User("username", "email", "password"));
		existent.addRole(userRole);
		dao.save(existent);
		
		// changes on entity
		existent.setApiKey("newApiKey");
		assertNotNull(existent.getId());
		
		existent = service.save(existent, false);
		
		List<Role> roles = roleDao.getForUser(existent);
		
		assertEquals(1, roles.size());
		assertEquals(userRole, roles.get(0));
	}
	
	@Test
	public void downgradeToNormalUser() throws LocalizedException {
		
		//before starting
		User existent = dao.save(new User("username", "email", "password"));
		existent.addRole(userRole);
		existent.addRole(adminRole);
		dao.save(existent);
		
		// changes on entity
		existent.setApiKey("newApiKey");
		assertNotNull(existent.getId());
		
		existent = service.save(existent, false);
		
		List<Role> roles = roleDao.getForUser(existent);
		
		assertEquals(1, roles.size());
		assertEquals(userRole, roles.get(0));
	}
	
	@Test
	public void updateAdminUser() throws LocalizedException {
		
		//before starting
		User existent = dao.save(new User("username", "email", "password"));
		existent.addRole(userRole);
		existent.addRole(adminRole);
		dao.save(existent);
		
		// changes on entity
		existent.setApiKey("newApiKey");
		assertNotNull(existent.getId());
		
		existent = service.save(existent, true);
		
		List<Role> roles = roleDao.getForUser(existent);
		
		assertEquals(2, roles.size());
		assertTrue(roles.contains(adminRole));
		assertTrue(roles.contains(userRole));
	}
	
	@Test
	public void upgradeToAdmin() throws LocalizedException {
		
		//before starting
		User existent = dao.save(new User("username", "email", "password"));
		existent.addRole(userRole);
		dao.save(existent);
		
		// changes on entity
		existent.setApiKey("newApiKey");
		assertNotNull(existent.getId());
		
		existent = service.save(existent, true);
		
		List<Role> roles = roleDao.getForUser(existent);
		
		assertEquals(2, roles.size());
		assertTrue(roles.contains(adminRole));
		assertTrue(roles.contains(userRole));
	}

}

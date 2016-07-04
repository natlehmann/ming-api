package ar.com.marcelomingrone.vericast.reports.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.Role;

public class RoleDaoTest extends AbstractTest {
	
	@Autowired
	private RoleDao dao;

	@Test
	public void getByNameNoResult() {
		assertNull(dao.getByName("name"));
	}
	
	@Test
	public void getByName() {
		Role role = builder.buildRole("name");
		assertEquals(role, dao.getByName("name"));
	}
	
	@Test
	public void getByNameCaseInsensitive() {
		Role role = new Role();
		role.setName("NAME");
		role = dao.save(role);
		assertEquals(role, dao.getByName("name"));
	}

}

package ar.com.marcelomingrone.vericast.reports.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.marcelomingrone.vericast.reports.dao.RoleDao;
import ar.com.marcelomingrone.vericast.reports.dao.UserDao;
import ar.com.marcelomingrone.vericast.reports.model.DuplicateUserException;
import ar.com.marcelomingrone.vericast.reports.model.LocalizedException;
import ar.com.marcelomingrone.vericast.reports.model.Role;
import ar.com.marcelomingrone.vericast.reports.model.RoleNames;
import ar.com.marcelomingrone.vericast.reports.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserDao dao;
	
	@Autowired
	private RoleDao roleDao;
	
	public User save(User user, boolean isAdmin) throws LocalizedException {
		
		User existing = dao.getByUsername(user.getUsername());
		if (existing != null && (user.getId() == null || !user.getId().equals(existing.getId()))) {
			throw new DuplicateUserException();
		}
		
		User existingEmail = dao.getByEmail(user.getEmail());
		if (existingEmail != null && (user.getId() == null || !user.getId().equals(existing.getId())) ) {
			throw new DuplicateUserException("error.duplicate.email");
		}
		
		Role adminRole = roleDao.getByName(RoleNames.ADMINISTRATOR.toString());
		Role userRole = roleDao.getByName(RoleNames.REPORT.toString());
		
		user.setRoles(null);
		
		user.addRole(userRole);
		if (isAdmin) {
			user.addRole(adminRole);
		}
		
		return dao.save(user);
		
	}

	public List<User> getAllPaginatedAndFiltered(int start, int count,
			String orderField, String orderDirection, String filter) {
		return dao.getAllPaginatedAndFiltered(start, count, orderField, orderDirection, filter);
	}

	public long getCount(String filter) {
		return dao.getCount(filter);
	}

	public User getById(Long id) {
		return dao.getById(id);
	}

	public void delete(Long id) {
		dao.delete(id);		
	}

}

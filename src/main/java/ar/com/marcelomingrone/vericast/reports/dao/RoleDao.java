package ar.com.marcelomingrone.vericast.reports.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.model.Role;
import ar.com.marcelomingrone.vericast.reports.model.User;

@Repository
public class RoleDao extends AbstractEntityDao<Role> {
	
	private static Log log = LogFactory.getLog(RoleDao.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected RoleDao() {
		super(Role.class);
	}


	@Override
	protected String getFilterQuery() {
		return ALIAS + ".name like " + FILTER_PARAM;
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	
	@Transactional
	@Override
	public Role save(Role entidad) {
		entidad.setName(entidad.getName().toLowerCase());
		return super.save(entidad);
	}


	@Transactional
	public Role getByName(String name) {
		
		Role role = null;
		
		try {
			role = (Role) sessionFactory.getCurrentSession().createQuery(
					"SELECT e FROM Role e WHERE e.name = :name")
					.setParameter("name", name.toLowerCase()).uniqueResult();
			
		} catch (NonUniqueResultException e) {
			log.error("Se encontro mas de un rol con el mismo nombre", e);
			
		} catch (NoResultException e) {}
		
		return role;
	}


	@SuppressWarnings("unchecked")
	@Transactional
	public List<Role> getForUser(User user) {
		return sessionFactory.getCurrentSession().createQuery(
				"SELECT u.roles FROM User u WHERE u.id = :id").setParameter("id", user.getId()).list();
	}

}

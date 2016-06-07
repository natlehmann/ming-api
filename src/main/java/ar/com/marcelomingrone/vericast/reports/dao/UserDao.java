package ar.com.marcelomingrone.vericast.reports.dao;

import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.model.User;

@Repository
public class UserDao extends AbstractEntityDao<User> {
	
	private static Log log = LogFactory.getLog(UserDao.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	protected UserDao() {
		super(User.class);
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Transactional(value="transactionManager")
	public User getByUsername(String username){
		
		User user = null;
		
		try {
			user = (User)sessionFactory.getCurrentSession().createQuery(
					"SELECT u FROM User u WHERE u.username = :username")
					.setParameter("username", username).uniqueResult();
			
		} catch (NonUniqueResultException e) {
			log.error("Se encontro mas de un usuario con nombre " + username);
			
		} catch (NoResultException e) {
			// nothing to do
		}
		
		return user;
	}

}

package ar.com.marcelomingrone.vericast.reports.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;

@Repository
public class ChannelDao extends AbstractEntityDao<Channel> {
	
	private static Log log = LogFactory.getLog(ChannelDao.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	protected ChannelDao() {
		super(Channel.class);
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Transactional
	public Channel findOrSave(Channel channel) {
		
		Channel found = getByKeyname(channel.getKeyname());
		
		if (found == null) {
			found = save(channel);
		}
		
		return found;
	}
	
	@Transactional
	public Channel getByKeyname(String keyname) {
		
		Channel channel = null;
		try {
			channel = (Channel) sessionFactory.getCurrentSession().createQuery(
					"SELECT c FROM Channel c WHERE c.keyname = :keyname")
					.setParameter("keyname", keyname).uniqueResult();
		
		} catch (NonUniqueResultException e) {
			log.error("Se encontro mas de un canal con el mismo keyname", e);
		}
		
		return channel;
	}

}

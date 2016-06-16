package ar.com.marcelomingrone.vericast.reports.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;

@Repository
public class PlaycountByChannelDao extends
		AbstractEntityDao<PlaycountByChannel> {
	
	@Autowired
	private SessionFactory sessionFactory;

	protected PlaycountByChannelDao() {
		super(PlaycountByChannel.class);
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}

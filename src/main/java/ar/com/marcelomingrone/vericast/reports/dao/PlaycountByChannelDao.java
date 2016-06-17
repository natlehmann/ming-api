package ar.com.marcelomingrone.vericast.reports.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;

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
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<PlaycountByChannel> getByReportItem(ReportItem item) {
		
		return sessionFactory.getCurrentSession().createQuery(
				"SELECT e FROM PlaycountByChannel e WHERE e.reportItem = :item")
				.setParameter("item", item).list();
	}

}

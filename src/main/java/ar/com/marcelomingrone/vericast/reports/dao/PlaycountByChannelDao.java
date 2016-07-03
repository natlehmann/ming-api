package ar.com.marcelomingrone.vericast.reports.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;

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

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Channel> getChannelsForReport(Long id) {
		
		return sessionFactory.getCurrentSession().createQuery(
				"SELECT DISTINCT(p.channel) FROM PlaycountByChannel p "
				+ "WHERE p.reportItem.report.id = :idReport")
				.setParameter("idReport", id).list();
	}

	@Override
	protected String getFilterQuery() {
		return ALIAS + ".channel.name like " + FILTER_PARAM;
	}

}

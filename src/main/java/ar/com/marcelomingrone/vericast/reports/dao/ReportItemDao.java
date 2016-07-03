package ar.com.marcelomingrone.vericast.reports.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;

@Repository
public class ReportItemDao extends AbstractEntityDao<ReportItem> {
	
	private static Log log = LogFactory.getLog(ReportItemDao.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	protected ReportItemDao() {
		super(ReportItem.class);
	}
	
	@Transactional
	public ReportItem getByTrackId(Report report, String bmatId) {
		
		ReportItem item = null;
		
		try {
			item = (ReportItem) sessionFactory.getCurrentSession().createQuery(
				"SELECT r FROM ReportItem r WHERE r.report = :report AND r.trackId = :trackId")
				.setParameter("trackId", bmatId)
				.setParameter("report", report)
				.uniqueResult();
			
		} catch (NonUniqueResultException e) {
			log.error("Se encontro mas de un track con el mismo id para el mismo reporte", e);
		} 
		
		return item;
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	protected String getFilterQuery() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("(").append(ALIAS).append(".trackName like ").append(FILTER_PARAM)
				.append(" OR ").append(ALIAS).append(".artistName like ").append(FILTER_PARAM)
				.append(" OR ").append(ALIAS).append(".labelName like ").append(FILTER_PARAM).append(")");
		
		return buffer.toString();
	}

}

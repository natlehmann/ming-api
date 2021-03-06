package ar.com.marcelomingrone.vericast.reports.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.Report.State;

@Repository
public class ReportDao extends AbstractEntityDao<Report> {
	
	private static Log log = LogFactory.getLog(ReportDao.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	protected ReportDao() {
		super(Report.class);
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Transactional
	public Report getByIdWithItems(Long id) {
		Report report = getById(id);
		if (report != null && report.getItems() != null) {
			report.getItems().size();
		}
		
		return report;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Report getByIdWithItemsAndPlaycounts(Long id) {
		
		Report report = getById(id);
		
		if (report != null) {
			List<ReportItem> items = sessionFactory.getCurrentSession().createQuery(
					"SELECT r FROM ReportItem r where r.report = :report "
					+ "ORDER BY r.totalPlayCount DESC, r.trackName ASC")
					.setParameter("report", report).list();
			
			for (ReportItem item : items) {
				if (item.getPlaycounts() != null) {
					item.getPlaycounts().size();
				}
			}
			
			report.setItems(items);
		}
		
		return report;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Report> getUnfinishedReports(User user) {
		
		return sessionFactory.getCurrentSession().createQuery(
				"SELECT r FROM Report r WHERE r.owner = :user AND r.state IN (:states)")
				.setParameter("user", user)
				.setParameterList("states", new State[]{State.IN_PROCESS, State.FINISHED})
				.list();
	}

	@Transactional
	public Report getReport(User user, String timePeriod, Date endDate) {
		
		Report report = null;
		try {
			report = (Report) sessionFactory.getCurrentSession().createQuery(
					"SELECT r FROM Report r WHERE r.owner = :user AND r.timePeriod = :timePeriod "
					+ "AND r.endDate = :endDate")
					.setParameter("user", user)
					.setParameter("timePeriod", timePeriod)
					.setParameter("endDate", endDate)
					.uniqueResult();
			
		} catch (NoResultException e) {
			// nothing
		} catch (NonUniqueResultException e) {
			log.error("Se encontro mas de un reporte con iguales parametros para el mismo usuario", e);
		}
		
		return report;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Report> getReportsForCurrentUser(User currentUser, int start,
			int count, String filter, String orderField, String orderDirection) {
		
		Session session = getSessionFactory().getCurrentSession();
		
		Query query = null;
		
		if (StringUtils.isEmpty(filter)) {
			
			String queryStr = "select e from Report e where owner = :user";
			
			if ( !StringUtils.isEmpty(orderField) ) {
				queryStr += " order by " + orderField + " " + orderDirection;
			}
			
			query = session.createQuery(queryStr);
			
		} else {
			
			String queryStr = "select e from Report e where owner = :user AND " + getFilterQuery();
			
			if ( !StringUtils.isEmpty(orderField) ) {
				queryStr += " order by " + orderField + " " + orderDirection;
			}
			
			query = session.createQuery(queryStr)
					.setParameter("filter", "%" + filter + "%");
		}
		
		return query
				.setParameter("user", currentUser)
				.setFirstResult(start)
				.setMaxResults(count).list();
	}

	@Transactional
	public long getReportsForCurrentUserCount(User currentUser, String filter) {
		
		
		Session session = getSessionFactory().getCurrentSession();
		
		Query query = null;

		if (StringUtils.isEmpty(filter)) {			
			
			String queryStr = "select count(e) from Report e where e.owner = :user";
			
			query = session.createQuery(queryStr);
			
		} else {
			
			String queryStr = "select count(e) from Report e where e.owner = :user AND "
					+ getFilterQuery();
			
			query = session.createQuery(queryStr)
					.setParameter("filter", "%" + filter + "%");
			
		}
		
		Long resultado = (Long) query.setParameter("user", currentUser).uniqueResult();
		
		return resultado != null ? resultado.longValue() : 0;
	}

	@Override
	protected String getFilterQuery() {
		return "(" + ALIAS + ".timePeriod like :filter OR " + ALIAS + ".state like :filter)";
	}

}

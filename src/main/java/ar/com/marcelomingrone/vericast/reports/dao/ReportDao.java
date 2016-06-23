package ar.com.marcelomingrone.vericast.reports.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;

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

}

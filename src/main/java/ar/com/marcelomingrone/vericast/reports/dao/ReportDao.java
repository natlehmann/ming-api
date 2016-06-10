package ar.com.marcelomingrone.vericast.reports.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.marcelomingrone.vericast.reports.model.Report;

@Repository
public class ReportDao extends AbstractEntityDao<Report> {
	
	@Autowired
	private SessionFactory sessionFactory;

	protected ReportDao() {
		super(Report.class);
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}

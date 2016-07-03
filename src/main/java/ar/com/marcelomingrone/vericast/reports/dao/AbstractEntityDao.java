package ar.com.marcelomingrone.vericast.reports.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

public abstract class AbstractEntityDao<T> {
	
	private static final String FILTER = "filter";
	protected static final String FILTER_PARAM = ":" + FILTER;
	protected static final String ALIAS = "e";
	

	@SuppressWarnings("rawtypes")
	private Class entityClass;
	
	private String entityName;
	
	private String sortField;
	
	@SuppressWarnings("rawtypes")
	protected AbstractEntityDao(Class entidad) {
		this.entityClass = entidad;
		this.entityName = this.entityClass.getName();
	}
	
	@SuppressWarnings("rawtypes")
	protected AbstractEntityDao(Class entidad, String sortField) {
		this(entidad);
		this.sortField = sortField;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(value="transactionManager")
	public List<T> getAll() {
		
		Session session = getSessionFactory().getCurrentSession();
		
		String query = "from " + this.entityName;
		if (this.sortField != null) {
			query += " order by " + this.sortField;
		}
		
		List result = session.createQuery(query).list();
		return result;
	}
	
	@Transactional(value="transactionManager")
	public T save(T entidad) {
		Session session = getSessionFactory().getCurrentSession();
		session.saveOrUpdate(entidad);
		
		return entidad;
	}
	
	@Transactional(value="transactionManager")
	public void deleteAll() {
		
		Session session = getSessionFactory().getCurrentSession();		
		session.createSQLQuery("delete from " + this.entityName).executeUpdate();		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(value="transactionManager")
	public T getById(Long id) {
		Session session = getSessionFactory().getCurrentSession();
		return (T) session.get(this.entityClass, id);
	}

	@Transactional(value="transactionManager")
	public List<T> getAllPaginated(int start,
			int count, String orderField,
			String orderDirection) {
		
		return getAllPaginatedAndFiltered(start, count, orderField, orderDirection, null);
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(value="transactionManager")
	public List<T> getAllPaginatedAndFiltered(int start,
			int count, String orderField,
			String orderDirection, String filter) {
		
		Session session = getSessionFactory().getCurrentSession();
		
		StringBuffer queryStr = new StringBuffer("SELECT ").append(ALIAS).append(" from ")
				.append(this.entityName).append(" ").append(ALIAS);
		
		if (!StringUtils.isEmpty(filter)) {
			queryStr.append(" WHERE ").append(getFilterQuery());
		}
		
		if ( !StringUtils.isEmpty(orderField) ) {
			queryStr.append(" order by ").append(orderField).append(" ").append(orderDirection);
		}
		
		Query query = session.createQuery(queryStr.toString());
		
		if (!StringUtils.isEmpty(filter)) {
			query.setParameter(FILTER, "%" + filter + "%");
		}
		
		return query.setFirstResult(start).setMaxResults(count).list();
	}

	protected abstract String getFilterQuery();

	@Transactional(value="transactionManager")
	public long getCount() {
		
		return getCount(null);
	}
	
	
	@Transactional(value="transactionManager")
	public long getCount(String filter) {
		
		Session session = getSessionFactory().getCurrentSession();
		
		StringBuffer queryStr = new StringBuffer("select count(").append(ALIAS).append(") from ")
									.append(this.entityName).append(" ").append(ALIAS);
		
		if (!StringUtils.isEmpty(filter)) {
			queryStr.append(" WHERE ").append(getFilterQuery());
		}
		
		Query query = session.createQuery(queryStr.toString());
		
		if (!StringUtils.isEmpty(filter)) {
			query.setParameter(FILTER, "%" + filter + "%");
		}
		
		Long resultado = (Long) query.uniqueResult();
		
		return resultado != null ? resultado.longValue() : 0;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(value="transactionManager")
	public void delete(Long id) {
		
		Session session = getSessionFactory().getCurrentSession();
		T entidad = (T) session.get(entityClass, id);
		session.delete(entidad);		
	}
	
	protected abstract SessionFactory getSessionFactory();
}

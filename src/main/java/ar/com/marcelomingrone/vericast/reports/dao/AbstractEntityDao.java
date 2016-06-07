package ar.com.marcelomingrone.vericast.reports.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

public abstract class AbstractEntityDao<T> {
	
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

	@SuppressWarnings("unchecked")
	@Transactional(value="transactionManager")
	public List<T> getAllPaginated(int inicio,
			int cantidadResultados, String campoOrdenamiento,
			String direccionOrdenamiento) {
		
		Session session = getSessionFactory().getCurrentSession();
		
		String query = "from " + this.entityName;
		
		if ( !StringUtils.isEmpty(campoOrdenamiento) ) {
			query += " order by " + campoOrdenamiento + " " + direccionOrdenamiento;
		}
		
		return session.createQuery(query)
				.setFirstResult(inicio).setMaxResults(cantidadResultados).list();
	}

	@Transactional(value="transactionManager")
	public long getCount() {
		
		Session session = getSessionFactory().getCurrentSession();
		
		String query = "select count(e) from " + this.entityName + " e";
		
		Long resultado = (Long) session.createQuery(query).uniqueResult();
		
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

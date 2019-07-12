package com.hepta.cliquemedicos.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;

public class SuperDAO<T, I extends Serializable> {

	private Class<T> persistedClass;

	public SuperDAO() {

	}

	public SuperDAO(Class<T> persistedClass) {
		this();
		this.persistedClass = persistedClass;
	}

	public T save(@Valid T entity) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return entity;
	}

	public T update(@Valid T newEntity) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		T entity = null;
		try {
			em.getTransaction().begin();
			entity = em.merge(newEntity);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return entity;
	}

	public Boolean delete(I id) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		Boolean success = false;
		if(id == null)
			return false;
		
		try {
			em.getTransaction().begin();
			T entity = find(id, em);
			T mergedEntity = em.merge(entity);
			em.remove(mergedEntity);
			em.getTransaction().commit();
			success = true;
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return success;
	}

	private T find(I id, EntityManager em) throws Exception {
		return em.find(persistedClass, id);
	}

	public T find(I id) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		T obj = null;
		try {
			obj = em.find(persistedClass, id);
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return obj;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> List<T> getAll() throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<T> list = new ArrayList<>();
		try {
			Query query = em.createQuery("FROM " + persistedClass.getName());
			list = query.getResultList();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> List<T> getPaginated(Integer initialPage, Integer maxValue) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<T> list = new ArrayList<>();

		try {
			StringBuilder sql = new StringBuilder("FROM ");
			sql.append(persistedClass.getName());
			Query query = em.createQuery(sql.toString());
			query.setFirstResult(initialPage - 1);
			query.setMaxResults(maxValue);
			list = query.getResultList();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return list;
	}

	public Long count() throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		Long result = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT COUNT(obj.id) FROM ");
			sql.append(persistedClass.getName());
			sql.append(" AS obj ");

			Query query = em.createQuery(sql.toString());
			result = (Long) query.getSingleResult();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return result;
	}

}

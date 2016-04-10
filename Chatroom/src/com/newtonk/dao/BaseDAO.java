package com.newtonk.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

public interface BaseDAO<T> {
	public SessionFactory getSessionFactory();

	T get(Class<T> entityClazz, Serializable id);

	void saveOrUpadate(T entity);

	Serializable save(T entity);

	void update(T entity);

	void delete(T entity);

	void delete(Class<T> entityClazz, Serializable id);

	List<T> findAll(Class<T> entityClazz);

	long findCount(Class<T> entityClazz);
}

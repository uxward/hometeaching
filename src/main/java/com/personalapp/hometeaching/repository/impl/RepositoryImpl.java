package com.personalapp.hometeaching.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.personalapp.hometeaching.repository.Repository;

@Transactional
public abstract class RepositoryImpl<ENTITY, ID extends Serializable> implements Repository<ENTITY, ID> {

	/**
	 * Defines the batch size for executing batch insert operations. This is the
	 * interval at which to flush results to the database (make a round trip).
	 * This should match the setting for hibernate.jdbc.batch_size in the
	 * application.properties file.
	 */
	public static final int BATCH_SIZE = 50;

	private final Class<ENTITY> entityClass;

	@PersistenceContext
	protected EntityManager em;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RepositoryImpl() {
		Class clazz = getClass();
		if (!(clazz == RepositoryImpl.class)) {
			while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
				clazz = clazz.getSuperclass();
			}
			entityClass = (Class<ENTITY>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
		} else {
			entityClass = (Class<ENTITY>) Object.class;
		}
	}

	@Override
	public ENTITY findById(ID id) {
		return em.find(entityClass, id);
	}

	@Override
	public void remove(ENTITY model) {
		em.remove(em.merge(model));
		em.flush();
	}

	@Override
	public void save(ENTITY model) {
		em.persist(model);
	}

	@Override
	public void save(Iterable<ENTITY> models) {
		batchAdd(em, entityClass, models);
	}

	@Override
	public void update(ENTITY model) {
		save(em.merge(model));
	}

	protected JPAQuery jpaFrom(EntityPath<?>... o) {
		return new JPAQuery(em).from(o);
	}

	/**
	 * Method that persists the provided list of batchEntities in batch mode.
	 * 
	 * @param <T>
	 * @param em
	 * @param clazz
	 * @param batchEntities
	 */
	public static <T extends Object> void batchAdd(EntityManager em, Class<T> clazz, Iterable<T> batchEntities) {
		BatchExecutor<T> addExector = new BatchExecutor<T>() {
			public void execute(EntityManager em, T entity) {
				em.persist(entity);
			}
		};

		batchOperation(em, clazz, batchEntities, addExector);

	}

	/**
	 * Method that removes the provided list of batchEntities in batch mode.
	 * 
	 * @param <T>
	 * @param em
	 * @param clazz
	 * @param batchEntities
	 */
	public static <T extends Object> void batchOperation(EntityManager em, Class<T> clazz, Iterable<T> batchEntities, BatchExecutor<T> batchOperation) {
		int batchCount = 1;

		for (T entity : batchEntities) {
			batchOperation.execute(em, entity);

			if (batchCount % BATCH_SIZE == 0) {
				em.flush();
			}
			batchCount++;
		}
		em.flush();
	}

	/**
	 * Defines an operation to execute in batch mode.
	 */
	public static interface BatchExecutor<T extends Object> {
		/**
		 * The operation method that will be executed in batch.
		 * 
		 * @param em
		 * @param entity
		 */
		public void execute(EntityManager em, T entity);
	}

}

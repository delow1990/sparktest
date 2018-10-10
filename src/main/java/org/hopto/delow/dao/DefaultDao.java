package org.hopto.delow.dao;

import javax.persistence.EntityManager;

public interface DefaultDao<T> {

    T createEntity(EntityManager entityManager, T entity);

    T findById(EntityManager entityManager, String entityId);

    T updateEntity(EntityManager entityManager, T entity);

    void deleteById(EntityManager entityManager, String entityId);

}

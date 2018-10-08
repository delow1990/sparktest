package org.hopto.delow.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public enum JpaManager {
    INSTANCE;

    private EntityManagerFactory factory;

    JpaManager() {
        factory = Persistence.createEntityManagerFactory("test");
    }

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}

package org.hopto.delow.dao;

import org.hopto.delow.model.jpa.Client;

import javax.persistence.EntityManager;

public class ClientDao {

    public Client createClient(EntityManager entityManager, Client client) {
        return entityManager.merge(client);
    }

    public Client findById(EntityManager entityManager, String id) {
        return entityManager.find(Client.class, Integer.valueOf(id));
    }

    public Client updateClient(EntityManager entityManager, Client client) {
        return entityManager.merge(client);
    }

    public void deleteById(EntityManager entityManager, String id) {
        entityManager.createQuery("delete from Client where id = :id")
                .setParameter("id", Integer.valueOf(id))
                .executeUpdate();
    }
}

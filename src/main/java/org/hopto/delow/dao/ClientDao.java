package org.hopto.delow.dao;

import org.hopto.delow.model.jpa.Client;

import javax.persistence.EntityManager;

public class ClientDao implements DefaultDao<Client> {

    @Override
    public Client createEntity(EntityManager entityManager, Client client) {
        return entityManager.merge(client);
    }

    @Override
    public Client findById(EntityManager entityManager, String id) {
        return entityManager.find(Client.class, Integer.valueOf(id));
    }

    @Override
    public Client updateEntity(EntityManager entityManager, Client client) {
        return entityManager.merge(client);
    }

    public void deactivateClient(EntityManager entityManager, String clientId) {
        entityManager.createQuery("update Client set active = false where id = :id")
                .setParameter("id", Integer.valueOf(clientId))
                .executeUpdate();
    }

    @Override
    public void deleteById(EntityManager entityManager, String id) {
        entityManager.createQuery("delete from Client where id = :id")
                .setParameter("id", Integer.valueOf(id))
                .executeUpdate();
    }
}

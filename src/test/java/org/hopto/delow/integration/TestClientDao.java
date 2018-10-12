package org.hopto.delow.integration;

import org.hopto.delow.dao.ClientDao;
import org.hopto.delow.model.jpa.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestClientDao {

    private EntityManagerFactory entityManagerFactory;
    private ClientDao clientDao;

    @BeforeAll
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        clientDao = new ClientDao();
    }

    @Test
    void testCreate() {
        Client client = getTestClientTemplate();

        Client createdClient = persistClient(client);

        assertNotNull(createdClient.getId());
    }

    @Test
    void testFindById() {
        Client client = getTestClientTemplate();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Client createdClient = clientDao.createEntity(entityManager, client);

        transaction.commit();
        entityManager.clear();

        Integer idToLookup = createdClient.getId();

        transaction.begin();

        Client foundClient = clientDao.findById(entityManager, idToLookup.toString());

        transaction.commit();

        assertNotNull(foundClient);
        assertEquals(idToLookup, foundClient.getId());
    }

    @Test
    void testDeactivateClient() {

        Client client = getTestClientTemplate();
        client = persistClient(client);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();


        clientDao.deactivateClient(entityManager, client.getId().toString());

        transaction.commit();

        Client deactivatedClient = clientDao.findById(entityManager, client.getId().toString());
        assertEquals(false, deactivatedClient.isActive());
    }

    private Client persistClient(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        client = clientDao.createEntity(entityManager, client);

        transaction.commit();
        entityManager.close();
        return client;
    }

    private Client getTestClientTemplate() {
        Client client = new Client();
        client.setFirstName("Name");
        client.setLastName("LastName");
        client.setMiddleName("MiddleName");
        client.setEmail("test@test.test");
        client.setPhoneNumber("88888888888");
        client.setBirthday(LocalDate.of(1990, 1, 1));
        return client;
    }

}

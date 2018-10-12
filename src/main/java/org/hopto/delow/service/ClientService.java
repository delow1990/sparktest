package org.hopto.delow.service;

import org.hopto.delow.dao.ClientDao;
import org.hopto.delow.dao.JpaManager;
import org.hopto.delow.model.jpa.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private ClientDao clientDao;

    private ThreadLocal<EntityManager> em;

    public ClientService(ClientDao clientDao, EntityManagerFactory entityManagerFactory) {
        this.clientDao = clientDao;
        em = ThreadLocal.withInitial(entityManagerFactory::createEntityManager);
    }

    public Client createClient(String firstName, String lastName, String middleName, LocalDate birthday, String email, String phone) {
        EntityManager entityManager = em.get();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setMiddleName(middleName);
        client.setBirthday(birthday);
        client.setEmail(email);
        client.setPhoneNumber(phone);

        client = clientDao.createEntity(entityManager, client);
        transaction.commit();
        logger.info("Created client: {}", client);
        return client;
    }

    public Client updateClient(String id, String firstName, String lastName, String middleName, LocalDate birthday, String email, String phone) {
        EntityManager entityManager = em.get();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Client client = clientDao.findById(entityManager, id);
        if (!isBlank(firstName))
            client.setFirstName(firstName);
        if (!isBlank(lastName))
            client.setLastName(lastName);
        if (!isBlank(middleName))
            client.setMiddleName(middleName);
        if (!isBlank(email))
            client.setEmail(email);
        if (!isBlank(phone))
            client.setPhoneNumber(phone);
        if (birthday != null)
            client.setBirthday(birthday);
        client = clientDao.updateEntity(entityManager, client);

        transaction.commit();
        return client;
    }

    public void deleteClient(String id) {
        EntityManager entityManager = em.get();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        clientDao.deactivateClient(entityManager, id);

        transaction.commit();
    }

    public Client getClient(String id) {
        EntityManager entityManager = em.get();
        return clientDao.findById(entityManager, id);
    }

}

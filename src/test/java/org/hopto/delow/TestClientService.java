package org.hopto.delow;

import org.hopto.delow.dao.ClientDao;
import org.hopto.delow.model.jpa.Client;
import org.hopto.delow.service.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class TestClientService {


    EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
    EntityManager entityManager = mock(EntityManager.class);
    EntityTransaction entityTransaction = mock(EntityTransaction.class);
    ClientDao dao = mock(ClientDao.class);
    Client returnedClient;

    @BeforeEach
    public void beforeEach() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        doNothing().when(entityTransaction).begin();
        doNothing().when(entityTransaction).commit();

        returnedClient = new Client();
        returnedClient.setId(1);
        returnedClient.setFirstName("stub");
        returnedClient.setLastName("test");
        returnedClient.setMiddleName("whatever");
        returnedClient.setBirthday(LocalDate.now());
        returnedClient.setPhoneNumber("89999999999");
        returnedClient.setEmail("test@test.test");

    }

    @Test
    public void testClientRetrieval() {
        final String id = returnedClient.getId().toString();

        when(dao.findById(any(EntityManager.class), eq(id))).thenReturn(returnedClient);

        ClientService service = new ClientService(dao, entityManagerFactory);
        Client client = service.getClient(id);

        verify(dao, times(1)).findById(any(EntityManager.class), eq(id));
        Assertions.assertEquals(id, client.getId().toString());
    }


    @Test
    public void testClientCreation() {

    }


}

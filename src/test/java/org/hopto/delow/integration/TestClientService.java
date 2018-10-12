package org.hopto.delow.integration;

import org.hopto.delow.dao.ClientDao;
import org.hopto.delow.model.jpa.Client;
import org.hopto.delow.service.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestClientService {

    private ClientService clientService;

    @BeforeAll
    void init() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
        ClientDao dao = new ClientDao();
        clientService = new ClientService(dao, factory);
    }

    @Test
    void testCreate() {
        Client client = clientService.createClient("Name", "LastName", "MiddleName", LocalDate.of(1990, 1, 1), "test@test.test", "88888888888");

        assertNotNull(client);
        assertNotNull(client.getId());
    }


}

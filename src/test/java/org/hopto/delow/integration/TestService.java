package org.hopto.delow.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.hopto.delow.TestApplication;
import org.hopto.delow.headers.ContentType;
import org.hopto.delow.model.rest.DefaultResponse;
import org.hopto.delow.service.exception.InvalidCardDataException;
import org.hopto.delow.service.exception.NoSuchCardException;
import org.hopto.delow.service.exception.NotEnoughMoneyException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestService {

    private HttpClient client;

    @BeforeAll
    void init() throws Exception {
        TestApplication.main(new String[]{"test"});
        client = new HttpClient();
        client.start();
    }

    @Test
    void createClient() throws InterruptedException, ExecutionException, TimeoutException {
        ContentResponse send = client.POST("http://localhost:8081/api/client/create")
                .content(new StringContentProvider(
                        "{" +
                                "\"firstName\":\"TEST\"," +
                                "\"lastName\":\"STUB\"," +
                                "\"middleName\":\"LOL\"," +
                                "\"birthday\":\"1990/09/01\"," +
                                "\"phoneNumber\":\"89824639045\"," +
                                "\"email\":\"delow19@gmail.com\"" +
                        "}"
                ), ContentType.APPLICATION_JSON)
                .send();

        assertEquals(200, send.getStatus());

    }

    @Test
    void createAccount() throws InterruptedException, ExecutionException, TimeoutException {
        ContentResponse send = client.POST("http://localhost:8081/api/account/create")
                .content(new StringContentProvider(
                        "{" +
                                "\"clientId\": \"1\",\n" +
                                "\"currencyType\": \"RUB\"\n" +
                        "}"
                ), ContentType.APPLICATION_JSON)
                .send();
        assertEquals(200, send.getStatus());
    }

    @Test
    void createCard() throws InterruptedException, ExecutionException, TimeoutException {
        ContentResponse send = client.POST("http://localhost:8081/api/card/create")
                .content(new StringContentProvider(
                        "{" +
                                "\"accountId\": \"1\"," +
                                "\"cardHolder\": \"FIRSTNAME1 LASTNAME1\"," +
                                "\"cardType\": \"DEBIT\"" +
                        "}"
                ), ContentType.APPLICATION_JSON)
                .send();
        assertEquals(200, send.getStatus());
    }

    @Test
    void testAddition() throws InterruptedException, ExecutionException, TimeoutException {
        ContentResponse send = client.POST("http://localhost:8081/api/transaction/create")
                .content(new StringContentProvider(
                        "{" +
                                "\"toCardNumber\":\"5323710000000001\"," +
                                "\"sum\":\"2000\"" +
                        "}"
                ), ContentType.APPLICATION_JSON)
                .send();
        assertEquals(200, send.getStatus());
    }

    @Test
    void testTransfer() throws InterruptedException, ExecutionException, TimeoutException {
        ContentResponse send = client.POST("http://localhost:8081/api/transaction/create")
                .content(new StringContentProvider(
                        "{" +
                                "\"toCardNumber\":\"5323710000000002\"," +
                                "\"sum\":\"2000\"," +
                                "\"fromCard\":{" +
                                    "\"number\":\"5323710000000001\"," +
                                    "\"cardHolder\":\"FIRSTNAME1 LASTNAME1\"," +
                                    "\"expDate\":\"10/2021\"," +
                                    "\"key\":\"123\"" +
                                "}" +
                        "}"
                ), ContentType.APPLICATION_JSON)
                .send();
        assertEquals(200, send.getStatus());
    }

    @Test
    void testTransferTooMuch() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        ContentResponse send = client.POST("http://localhost:8081/api/transaction/create")
                .content(new StringContentProvider(
                        "{" +
                                "\"toCardNumber\":\"5323710000000002\"," +
                                "\"sum\":\"200000\"," +
                                "\"fromCard\":{" +
                                    "\"number\":\"5323710000000001\"," +
                                    "\"cardHolder\":\"FIRSTNAME1 LASTNAME1\"," +
                                    "\"expDate\":\"10/2021\"," +
                                    "\"key\":\"123\"" +
                                "}" +
                        "}"
                ), ContentType.APPLICATION_JSON)
                .send();
        assertEquals(400, send.getStatus());
        ObjectMapper mapper = new ObjectMapper();
        DefaultResponse defaultResponse = mapper.readValue(send.getContent(), DefaultResponse.class);
        assertEquals(new NotEnoughMoneyException().getResponseCode(), defaultResponse.getResponseCode().intValue());
    }

    @Test
    void testTransferWrongCardInfo() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        ContentResponse send = client.POST("http://localhost:8081/api/transaction/create")
                .content(new StringContentProvider(
                        "{" +
                                "\"toCardNumber\":\"5323710000000002\"," +
                                "\"sum\":\"200000\"," +
                                "\"fromCard\":{" +
                                 "\"number\":\"5323710000000001\"," +
                                    "\"cardHolder\":\"FIRSTNAME1 LASTNAME1\"," +
                                    "\"expDate\":\"10/2021\"," +
                                    "\"key\":\"132\"" +
                                "}" +
                                "}"
                ), ContentType.APPLICATION_JSON)
                .send();
        assertEquals(400, send.getStatus());
        ObjectMapper mapper = new ObjectMapper();
        DefaultResponse defaultResponse = mapper.readValue(send.getContent(), DefaultResponse.class);
        assertEquals(new InvalidCardDataException().getResponseCode(), defaultResponse.getResponseCode().intValue());
    }

    @Test
    void testTransferWrongCardNumber() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        ContentResponse send = client.POST("http://localhost:8081/api/transaction/create")
                .content(new StringContentProvider(
                        "{" +
                                "\"toCardNumber\":\"5323710000000004\"," +
                                "\"sum\":\"2000\"," +
                                "\"fromCard\":{" +
                                    "\"number\":\"5323710000000001\"," +
                                    "\"cardHolder\":\"FIRSTNAME1 LASTNAME1\"," +
                                    "\"expDate\":\"10/2021\"," +
                                    "\"key\":\"123\"" +
                                "}" +
                        "}"
                ), ContentType.APPLICATION_JSON)
                .send();
        assertEquals(400, send.getStatus());
        ObjectMapper mapper = new ObjectMapper();
        DefaultResponse defaultResponse = mapper.readValue(send.getContent(), DefaultResponse.class);
        assertEquals(new NoSuchCardException().getResponseCode(), defaultResponse.getResponseCode().intValue());
    }

    @AfterAll
    void close() throws Exception {
        client.stop();
    }

}

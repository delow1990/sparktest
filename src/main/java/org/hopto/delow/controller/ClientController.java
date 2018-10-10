package org.hopto.delow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hopto.delow.model.jpa.Client;
import org.hopto.delow.model.rest.DefaultResponse;
import org.hopto.delow.model.rest.client.*;
import org.hopto.delow.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ObjectReader clientCreateRequestReader;
    private final ObjectReader clientUpdateRequestReader;
    private final ObjectReader clientDeleteRequestReader;

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        clientCreateRequestReader = mapper.readerFor(ClientCreateRequest.class);
        clientUpdateRequestReader = mapper.readerFor(ClientUpdateRequest.class);
        clientDeleteRequestReader = mapper.readerFor(ClientDeleteRequest.class);
    }

    public Object createClientHandler(Request request, Response response) throws IOException {
        ClientCreateRequest requestBody = clientCreateRequestReader.readValue(request.bodyAsBytes());
        Client client = clientService.createClient(requestBody.getFirstName(), requestBody.getLastName(), requestBody.getMiddleName(), requestBody.getBirthday(), requestBody.getEmail(), requestBody.getPhoneNumber());

        ClientCreateResponse responseBody = new ClientCreateResponse();
        responseBody.setId(client.getId().toString());
        responseBody.setResponseCode(0);
        responseBody.setResponseMessage("");
        return responseBody;
    }

    public Object updateClientHandler(Request request, Response response) throws IOException {
        ClientUpdateRequest requestBody = clientUpdateRequestReader.readValue(request.bodyAsBytes());
        Client client = clientService.updateClient(requestBody.getId(), requestBody.getFirstName(), requestBody.getLastName(), requestBody.getMiddleName(), requestBody.getBirthday(), requestBody.getEmail(), requestBody.getPhoneNumber());

        ClientUpdateResponse responseBody = new ClientUpdateResponse();
        responseBody.setId(client.getId().toString());
        responseBody.setResponseCode(0);
        responseBody.setResponseMessage("");
        return responseBody;
    }

    public Object deleteClientHandler(Request request, Response response) throws IOException {
        ClientDeleteRequest requestBody = clientDeleteRequestReader.readValue(request.bodyAsBytes());
        clientService.deleteClient(requestBody.getId());

        DefaultResponse responseBody = new DefaultResponse();
        responseBody.setResponseCode(0);
        responseBody.setResponseMessage("");
        return responseBody;
    }


    public Object getClientHandler(Request request, Response response) {
        String id = request.params("id");
        return clientService.getClient(id);
    }
}

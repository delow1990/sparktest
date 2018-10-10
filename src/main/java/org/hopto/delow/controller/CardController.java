package org.hopto.delow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hopto.delow.model.rest.DefaultResponse;
import org.hopto.delow.model.rest.card.CardCreateRequest;
import org.hopto.delow.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ObjectReader cardCreateRequestReader;

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        cardCreateRequestReader = mapper.readerFor(CardCreateRequest.class);

    }


    public Object createCardHandler(Request request, Response response) throws IOException {
        CardCreateRequest requestBody = cardCreateRequestReader.readValue(request.bodyAsBytes());
        cardService.createCard(requestBody.getAccountId(), requestBody.getCardHolder(), requestBody.getCardType());

        DefaultResponse responseBody = new DefaultResponse();
        responseBody.setResponseCode(0);
        responseBody.setResponseMessage("");

        return responseBody;
    }
}

package org.hopto.delow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hopto.delow.model.rest.DefaultResponse;
import org.hopto.delow.model.rest.transaction.CardInfo;
import org.hopto.delow.model.rest.transaction.TransactionCreateRequest;
import org.hopto.delow.service.exception.InvalidCardDataException;
import org.hopto.delow.service.exception.NoSuchCardException;
import org.hopto.delow.service.exception.NotEnoughMoneyException;
import org.hopto.delow.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ObjectMapper mapper;

    private final TransactionService transactionService;

    private final ObjectReader transcationCreateRequestReader;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        transcationCreateRequestReader = mapper.readerFor(TransactionCreateRequest.class);
    }

    public Object createTransactionHandler(Request request, Response response) throws IOException {

        TransactionCreateRequest requsetBody = transcationCreateRequestReader.readValue(request.bodyAsBytes());
        CardInfo fromCard = requsetBody.getFromCard();
        DefaultResponse responseBody = new DefaultResponse();
        try {
            if (fromCard != null)
                transactionService.createTransaction(fromCard.getNumber(), fromCard.getCardHolder(), fromCard.getExpDate(), fromCard.getKey(), requsetBody.getToCardNumber(), requsetBody.getSum());
            else
                transactionService.createTransaction(requsetBody.getToCardNumber(), requsetBody.getSum());
            responseBody.setResponseCode(0);
            responseBody.setResponseMessage("");
        } catch (NotEnoughMoneyException | InvalidCardDataException | NoSuchCardException e) {
            logger.info("Transaction request failed: responseCode: {}, responseMessage: {}", e.getResponseCode(), e.getMessage());
            response.status(400);
            responseBody.setResponseCode(e.getResponseCode());
            responseBody.setResponseMessage(e.getMessage());
        }
        return responseBody;
    }
}

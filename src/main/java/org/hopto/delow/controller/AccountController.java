package org.hopto.delow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hopto.delow.model.jpa.Account;
import org.hopto.delow.model.rest.DefaultResponse;
import org.hopto.delow.model.rest.DefaultResponseWithId;
import org.hopto.delow.model.rest.account.AccountBlockRequest;
import org.hopto.delow.model.rest.account.AccountCreateRequest;
import org.hopto.delow.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ObjectMapper mapper;

    private final ObjectReader accountCreateRequestReader;
    private final ObjectReader accountBlockRequestReader;

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService= accountService;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        accountCreateRequestReader = mapper.readerFor(AccountCreateRequest.class);
        accountBlockRequestReader = mapper.readerFor(AccountBlockRequest.class);
    }


    public Object createAccountHandler(Request request, Response response) throws IOException {
        AccountCreateRequest requestBody = accountCreateRequestReader.readValue(request.bodyAsBytes());
        Account account = accountService.createAccount(requestBody.getClientId(), requestBody.getCurrencyType());

        DefaultResponseWithId responseBody = new DefaultResponseWithId();
        responseBody.setId(account.getId().toString());
        responseBody.setResponseCode(0);
        responseBody.setResponseMessage("");
        return responseBody;
    }

    public Object blockAccountHandler(Request request, Response response) throws IOException {
        AccountBlockRequest requestBody = accountBlockRequestReader.readValue(request.bodyAsBytes());
        accountService.blockAccount(requestBody.getAccountId());

        DefaultResponse responseBody = new DefaultResponse();
        responseBody.setResponseCode(0);
        responseBody.setResponseMessage("");
        return responseBody;
    }
}

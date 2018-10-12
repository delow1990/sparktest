package org.hopto.delow;


import org.hopto.delow.controller.*;
import org.hopto.delow.converter.JsonOutConverter;
import org.hopto.delow.dao.*;
import org.hopto.delow.headers.ContentType;
import org.hopto.delow.model.rest.HelloResponse;
import org.hopto.delow.service.AccountService;
import org.hopto.delow.service.CardService;
import org.hopto.delow.service.ClientService;
import org.hopto.delow.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.QueryParamsMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.*;

public class TestApplication {

    private static Logger logger = LoggerFactory.getLogger(TestApplication.class);

    public static void main(String[] args) {

        JsonOutConverter converter = new JsonOutConverter();

        HelloWorldController controller = new HelloWorldController();

        ClientDao clientDao = new ClientDao();
        AccountDao accountDao = new AccountDao();
        CardDao cardDao = new CardDao();
        TransactionDao transactionDao = new TransactionDao();
        EntityManagerFactory factory;

        //well that's ugly
        if (args.length == 0)
            factory = JpaManager.INSTANCE.getFactory();
        else
            factory = Persistence.createEntityManagerFactory(args[0]);

        ClientService clientService = new ClientService(clientDao, factory);
        AccountService accountService = new AccountService(accountDao, clientDao, factory);
        CardService cardService = new CardService(clientDao, cardDao, accountDao, factory);
        TransactionService transactionService = new TransactionService(accountDao, cardDao, transactionDao, factory);

        ClientController clientController = new ClientController(clientService);
        AccountController accountController = new AccountController(accountService);
        CardController cardController = new CardController(cardService);
        TransactionController transactionController = new TransactionController(transactionService);


        port(8081);

        path("/api", () -> {
            before("/*",(request, response) -> {
                if (!request.contentType().startsWith(ContentType.APPLICATION_JSON)) halt(415);
                response.type(ContentType.APPLICATION_JSON_UTF8);
            });
            get("/hello", (request, response) -> {
                QueryParamsMap queryParamsMap = request.queryMap();
                queryParamsMap.toMap().forEach((key, value) -> logger.trace("Got param: {} with values: {}", key, value));
                response.status(200);
                return "";
            });
            post("/hello", (request, response) -> {
                QueryParamsMap queryParamsMap = request.queryMap();
                queryParamsMap.toMap().forEach((key, value) -> logger.trace("Got param: {} with values: {}", key, value));
                response.status(200);
                HelloResponse resp = new HelloResponse();
                resp.setMessage("Hello world!");
                return resp;
            }, converter);
            post("/h", controller::post, converter);
            path("/client", () -> {
                post("/create", clientController::createClientHandler, converter);
                post("/update", clientController::updateClientHandler, converter);
                post("/delete", clientController::deleteClientHandler, converter);
                get("/:id", clientController::getClientHandler, converter);
            });
            path("/account", () -> {
                post("/create", accountController::createAccountHandler, converter);
                post("/block", accountController::blockAccountHandler, converter);
            });
            path("/card", () -> {
                post("/create", cardController::createCardHandler, converter);
            });
            path("/transaction", () -> {
                post("/create", transactionController::createTransactionHandler, converter);
            });

        });
    }
}

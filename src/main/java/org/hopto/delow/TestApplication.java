package org.hopto.delow;


import org.hopto.delow.controller.AccountController;
import org.hopto.delow.controller.CardController;
import org.hopto.delow.controller.ClientController;
import org.hopto.delow.controller.HelloWorldController;
import org.hopto.delow.converter.JsonOutConverter;
import org.hopto.delow.dao.AccountDao;
import org.hopto.delow.dao.CardDao;
import org.hopto.delow.dao.ClientDao;
import org.hopto.delow.headers.ContentType;
import org.hopto.delow.model.rest.HelloResponse;
import org.hopto.delow.service.AccountService;
import org.hopto.delow.service.CardService;
import org.hopto.delow.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.QueryParamsMap;

import static spark.Spark.*;

public class TestApplication {

    private static Logger logger = LoggerFactory.getLogger(TestApplication.class);

    public static void main(String[] args) {

        JsonOutConverter converter = new JsonOutConverter();

        HelloWorldController controller = new HelloWorldController();

        ClientDao clientDao = new ClientDao();
        AccountDao accountDao = new AccountDao();
        CardDao cardDao = new CardDao();

        ClientService clientService = new ClientService(clientDao);
        AccountService accountService = new AccountService(accountDao, clientDao);
        CardService cardService = new CardService(clientDao, cardDao, accountDao);

        ClientController clientController = new ClientController(clientService);
        AccountController accountController = new AccountController(accountService);
        CardController cardController = new CardController(cardService);

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

        });
    }
}

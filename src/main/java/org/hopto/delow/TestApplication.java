package org.hopto.delow;


import org.hopto.delow.controller.ClientController;
import org.hopto.delow.controller.HelloWorldController;
import org.hopto.delow.converter.JsonOutConverter;
import org.hopto.delow.dao.ClientDao;
import org.hopto.delow.dao.JpaManager;
import org.hopto.delow.headers.ContentType;
import org.hopto.delow.model.rest.HelloResponse;
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

        ClientDao dao = new ClientDao();

        ClientService clientService = new ClientService(dao, JpaManager.INSTANCE.getFactory());

        ClientController clientController = new ClientController(clientService);

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
            post("/client/create", clientController::createClientHandler, converter);
            post("/client/update", clientController::updateClientHandler, converter);
            post("/client/delete", clientController::deleteClientHandler, converter);
            get("/client/:id", clientController::getClientHandler, converter);

        });
    }
}

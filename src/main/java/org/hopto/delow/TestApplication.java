package org.hopto.delow;


import org.hopto.delow.controller.HelloWorldController;
import org.hopto.delow.converter.JsonOutConverter;
import org.hopto.delow.headers.ContentType;
import org.hopto.delow.model.rest.HelloResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.QueryParamsMap;

import static spark.Spark.*;

public class TestApplication {

    private static Logger logger = LoggerFactory.getLogger(TestApplication.class);

    public static void main(String[] args) {

        JsonOutConverter converter = new JsonOutConverter();

        HelloWorldController controller = new HelloWorldController();

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

        });
    }
}

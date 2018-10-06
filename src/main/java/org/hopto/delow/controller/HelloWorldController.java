package org.hopto.delow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.hopto.delow.model.rest.HelloRequest;
import org.hopto.delow.model.rest.HelloResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class HelloWorldController {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    private final ObjectReader reader = new ObjectMapper().readerFor(HelloRequest.class);

    public Object post(Request request, Response response) throws IOException {
        HelloRequest requestBody = reader.readValue(request.bodyAsBytes());
        logger.trace("Got body: {}", requestBody);
        response.status(201);
        HelloResponse resp = new HelloResponse();
        resp.setMessage("Hello " + requestBody.getTarget() + "!");
        return resp;
    }

}

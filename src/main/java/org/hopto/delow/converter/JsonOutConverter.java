package org.hopto.delow.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

public class JsonOutConverter implements ResponseTransformer {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String render(Object model) throws Exception {
        return mapper.writeValueAsString(model);
    }

}

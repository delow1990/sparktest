package org.hopto.delow.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import spark.ResponseTransformer;

public class JsonOutConverter implements ResponseTransformer {

    private final ObjectWriter writer;

    public JsonOutConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        writer = mapper.writer();
    }

    @Override
    public String render(Object model) throws Exception {
        return writer.writeValueAsString(model);
    }

}

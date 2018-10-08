package org.hopto.delow.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class DefaultResponse {

    private Integer responseCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String responseMessage;

}

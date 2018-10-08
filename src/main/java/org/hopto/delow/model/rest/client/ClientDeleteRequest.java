package org.hopto.delow.model.rest.client;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ClientDeleteRequest {

    @NotEmpty
    private String id;

}

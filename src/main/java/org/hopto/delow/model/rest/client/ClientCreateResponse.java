package org.hopto.delow.model.rest.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hopto.delow.model.rest.DefaultResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientCreateResponse extends DefaultResponse {

    private String id;

}

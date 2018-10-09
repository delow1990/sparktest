package org.hopto.delow.model.rest.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hopto.delow.model.rest.DefaultResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientUpdateResponse extends DefaultResponse {

    private String id;

}

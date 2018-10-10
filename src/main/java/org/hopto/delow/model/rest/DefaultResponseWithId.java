package org.hopto.delow.model.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultResponseWithId extends DefaultResponse{

    private String id;

}

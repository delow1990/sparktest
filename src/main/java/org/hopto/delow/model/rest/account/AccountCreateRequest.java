package org.hopto.delow.model.rest.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hopto.delow.model.jpa.CurrencyType;

@Data
public class AccountCreateRequest {

    private String clientId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CurrencyType currencyType;

}

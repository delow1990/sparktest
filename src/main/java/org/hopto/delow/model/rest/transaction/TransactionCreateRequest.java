package org.hopto.delow.model.rest.transaction;

import lombok.Data;


@Data
public class TransactionCreateRequest {

    private CardInfo fromCard;

    private String toCardNumber;

    private Integer sum;

}


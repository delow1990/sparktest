package org.hopto.delow.model.rest.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hopto.delow.model.jpa.CardType;

@Data
public class CardCreateRequest {

    private String accountId;

    private String cardHolder;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CardType cardType;

}

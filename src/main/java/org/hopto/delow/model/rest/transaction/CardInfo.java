package org.hopto.delow.model.rest.transaction;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonDeserialize(using = CardInfoDeserializer.class)
public class CardInfo {

    private String number;

    private String cardHolder;

    private LocalDate expDate;

    private String key;

}

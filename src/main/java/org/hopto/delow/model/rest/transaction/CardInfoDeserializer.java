package org.hopto.delow.model.rest.transaction;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class CardInfoDeserializer extends StdDeserializer<CardInfo> {

    public CardInfoDeserializer()
    {
        super(CardInfo.class);
    }

    @Override
    public CardInfo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String number = node.get("number").asText();
        String cardHolder = node.get("cardHolder").asText();
        int key = node.get("key").asInt();
        byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(key).array();
        String codeHash = DigestUtils.sha256Hex(bytes);
        String[] expDateArray = node.get("expDate").asText().split("/");
        LocalDate expDate = LocalDate.of(Integer.valueOf(expDateArray[1]), Integer.valueOf(expDateArray[0]), 1).with(TemporalAdjusters.lastDayOfMonth());
        CardInfo cardInfo = new CardInfo();
        cardInfo.setNumber(number);
        cardInfo.setCardHolder(cardHolder);
        cardInfo.setExpDate(expDate);
        cardInfo.setKey(codeHash);
        return cardInfo;
    }
}

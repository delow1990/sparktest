package org.hopto.delow.service.exception;

public class InvalidCardDataException extends DefaultException {

    @Override
    public int getResponseCode() {
        return 98;
    }

    @Override
    public String getMessage() {
        return "Invalid card data!";
    }
}

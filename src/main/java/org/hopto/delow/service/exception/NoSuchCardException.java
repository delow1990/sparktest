package org.hopto.delow.service.exception;

public class NoSuchCardException extends DefaultException {

    @Override
    public int getResponseCode() {
        return 90;
    }

    @Override
    public String getMessage() {
        return "Wrong card number!";
    }
}

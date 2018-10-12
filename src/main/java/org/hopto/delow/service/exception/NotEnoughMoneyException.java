package org.hopto.delow.service.exception;

public class NotEnoughMoneyException extends DefaultException {

    @Override
    public int getResponseCode() {
        return 99;
    }

    @Override
    public String getMessage() {
        return "Not enough money!";
    }
}

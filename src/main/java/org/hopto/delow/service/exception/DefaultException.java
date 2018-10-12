package org.hopto.delow.service.exception;

public abstract class DefaultException extends Exception {

    public abstract int getResponseCode();

}

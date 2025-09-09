package com.fqts.mysociety.exception;

public class ChargeNotFoundException extends RuntimeException {
    public ChargeNotFoundException(String message) {
        super(message);
    }
}
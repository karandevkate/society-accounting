package com.fqts.mysociety.exception;

public class BillNotFoundException extends RuntimeException {
    public BillNotFoundException(String message) {
        super(message);
    }
}
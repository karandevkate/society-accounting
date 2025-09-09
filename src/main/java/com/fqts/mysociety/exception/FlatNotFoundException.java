package com.fqts.mysociety.exception;

public class FlatNotFoundException extends RuntimeException {

    public FlatNotFoundException(String flatNumber) {
        super("Flat not found with number: " + flatNumber);
    }
}

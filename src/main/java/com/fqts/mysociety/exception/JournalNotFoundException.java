package com.fqts.mysociety.exception;

public class JournalNotFoundException extends RuntimeException {
    public JournalNotFoundException(String message) {
        super(message);
    }
}

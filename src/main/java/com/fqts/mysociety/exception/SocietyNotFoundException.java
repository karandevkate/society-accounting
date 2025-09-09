package com.fqts.mysociety.exception;

import java.util.UUID;

public class SocietyNotFoundException extends RuntimeException {

    public SocietyNotFoundException(String message) {
        super(message);
    }

    public SocietyNotFoundException(UUID id) {
        super("Society not found with ID: " + id);
    }
}

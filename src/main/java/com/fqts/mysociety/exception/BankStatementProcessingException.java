package com.fqts.mysociety.exception;

public class BankStatementProcessingException extends RuntimeException {

  public BankStatementProcessingException(String message, Throwable cause) {
    super(message, cause);
  }

  public BankStatementProcessingException(String message) {
    super(message);
  }
}

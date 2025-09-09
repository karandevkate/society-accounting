package com.fqts.mysociety.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ChargeNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleChargeNotFound(ChargeNotFoundException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(FlatAlreadyAssignedException.class)
  public ResponseEntity<Map<String, String>> handleFlatAlreadyAssigned(FlatAlreadyAssignedException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(SocietyNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleSocietyNotFound(SocietyNotFoundException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(FlatNotFoundException.class)
  public ResponseEntity<String> handleFlatNotFound(FlatNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleInvalidCredentials(
      InvalidCredentialsException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(StaffNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleStaffNotFoundException(
      StaffNotFoundException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<Map<String, String>> handleCustomException(CustomException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", ex.getMessage());
    return new ResponseEntity<>(response, ex.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
    return buildErrorResponse("Internal Server Error: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }


  private ResponseEntity<Map<String, String>> buildErrorResponse(String message,
      HttpStatus status) {
    Map<String, String> response = new HashMap<>();
    response.put("error", message);
    return new ResponseEntity<>(response, status);
  }

  @ExceptionHandler(BankStatementProcessingException.class)
  public ResponseEntity<Map<String, String>> handleBankStatementProcessingException(
      BankStatementProcessingException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(ExpenseNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleExpenseNotFound(ExpenseNotFoundException ex) {
    return buildErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidSocietyRequestException.class)
  public ResponseEntity<Map<String, String>> handleInvalidSocietyRequestException(
          InvalidSocietyRequestException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidPaymentModeException.class)
  public ResponseEntity<Map<String, String>> handleInvalidPaymentModeException(
      InvalidPaymentModeException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ImageProcessingException.class)
  public ResponseEntity<Map<String, String>> handleImageProcessingException(
      ImageProcessingException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(FileProcessingException.class)
  public ResponseEntity<Map<String, String>> handleFileProcessingException(
      FileProcessingException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MatrixChargeNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleMatrixChargeNotFoundException(
      MatrixChargeNotFoundException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}




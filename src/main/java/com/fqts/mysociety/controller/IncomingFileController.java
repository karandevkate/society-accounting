package com.fqts.mysociety.controller;

import com.fqts.mysociety.exception.BankStatementProcessingException;
import com.fqts.mysociety.exception.FileProcessingException;
import com.fqts.mysociety.service.IncomingFileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(allowedHeaders = "*")
public class IncomingFileController {

  private static final Logger log = LoggerFactory.getLogger(IncomingFileController.class);
  private final IncomingFileService incomingFileService;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> uploadBankStatement(
      @RequestParam("file") MultipartFile file,
      @RequestParam("uploadedBy") String uploadedBy,
      @RequestParam("bankName") String bankName) {

    log.info(
        "Received request to upload bank statement. UploadedBy: {}, BankName: {}, FileName: {}",
        uploadedBy, bankName, file.getOriginalFilename());

    try {
      incomingFileService.uploadBankStatementFile(file, uploadedBy, bankName);
      log.info("Bank statement uploaded successfully. UploadedBy: {}, BankName: {}", uploadedBy,
          bankName);
      return ResponseEntity.ok("Bank statement uploaded successfully.");

    } catch (IllegalArgumentException e) {
      log.warn("Upload failed due to bad request: {}", e.getMessage());
      return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());

    } catch (BankStatementProcessingException | FileProcessingException e) {

      log.error("Bank processing failed: {}", e.getMessage(), e);
      return ResponseEntity.internalServerError().body("Bank processing error: " + e.getMessage());

    } catch (Exception e) {

      log.error("Unexpected error during file upload: {}", e.getMessage(), e);
      return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
    }
  }
}

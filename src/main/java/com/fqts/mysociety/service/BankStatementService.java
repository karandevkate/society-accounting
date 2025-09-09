package com.fqts.mysociety.service;

import com.fqts.mysociety.model.IncomingFile;
import org.springframework.web.multipart.MultipartFile;

public interface BankStatementService {

  void processAndSaveStatements(IncomingFile incomingFile, MultipartFile file);
  
}
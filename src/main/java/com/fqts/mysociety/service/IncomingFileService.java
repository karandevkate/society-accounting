package com.fqts.mysociety.service;

import org.springframework.web.multipart.MultipartFile;

public interface IncomingFileService {

  void uploadBankStatementFile(MultipartFile file, String uploadedBy, String bankName);
}

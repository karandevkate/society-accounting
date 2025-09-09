package com.fqts.mysociety.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  String saveFile(MultipartFile file, String folderName) throws IOException;

  String saveGovIdPhoto(MultipartFile govIdPhoto) throws IOException;

  String saveUserPhoto(MultipartFile staffPhoto) throws IOException;
}

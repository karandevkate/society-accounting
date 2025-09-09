package com.fqts.mysociety.helper;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

public class ChecksumHelper {

  public static String calculateMD5Checksum(MultipartFile file) throws IOException {
    try (InputStream inputStream = file.getInputStream()) {
      return DigestUtils.md5Hex(inputStream);
    }
  }

  public static String calculateMD5Checksum(InputStream inputStream) throws IOException {
    return DigestUtils.md5Hex(inputStream);
  }
}

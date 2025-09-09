package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String saveFile(MultipartFile file, String folderName) throws IOException {
        String uploadDir = "uploads/" + folderName;
        Files.createDirectories(Paths.get(uploadDir));

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        Path filePath = Paths.get(uploadDir, uniqueFileName);

        Files.write(filePath, file.getBytes());

        return  folderName + "/" + uniqueFileName;
    }

    public String saveGovIdPhoto(MultipartFile govIdPhoto) throws IOException {
        return saveFile(govIdPhoto, "govIdProof");
    }

    @Override
    public String saveUserPhoto(MultipartFile staffPhoto) throws IOException {
        return saveFile(staffPhoto, "userProof");
    }
}

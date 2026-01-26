package com.ChefsAlchemy.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {// inject upload dir from app.properties
        this.fileStorageLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(fileStorageLocation);// create dic if not exist
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create directory to store uploaded files", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));// clean
                                                                                                            // file name
        String fileNameExtension = ".";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileNameExtension = originalFileName.substring(dotIndex);
        }

        String fileName = UUID.randomUUID().toString() + fileNameExtension; // generate random file name
        try {// check if file name contains invalid path sequence
            if (fileName.contains("..")) {
                throw new RuntimeException("Filename contains invalid path sequence" + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName); // copy file to target location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);// replace existing
                                                                                                   // file with same
                                                                                                   // name
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("could notto store file " + fileName + "please try again", ex);
        }

    }

    public Resource loadFileAsResource(String fileName) {// load file as resouce
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) { // check if file exists and is readable
                return resource;
            } else {
                throw new RuntimeException("Could not read the file where file not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete the file: " + fileName, ex);
        }
    }

}

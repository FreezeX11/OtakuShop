package com.Backend.ServiceInterface;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    String uploadFile(MultipartFile file, String path) throws IOException;
    void deleteFile(String filename, String path) throws IOException;
}

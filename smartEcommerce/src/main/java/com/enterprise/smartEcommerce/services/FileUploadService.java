package com.enterprise.smartEcommerce.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileUploadService {
    // 👇 Renamed from uploadImage to uploadFile 👇
    String uploadFile(MultipartFile file) throws IOException;
}
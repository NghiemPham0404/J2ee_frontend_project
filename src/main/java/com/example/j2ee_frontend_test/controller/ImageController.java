package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // Chuyển file thành Base64
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

            // Gọi service để upload ảnh
            return imageService.uploadImage(base64Image);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}


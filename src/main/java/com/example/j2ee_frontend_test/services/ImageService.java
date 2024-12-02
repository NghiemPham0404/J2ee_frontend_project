package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.services.apis.ImageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ImageService {

    @Autowired
    ImageApi imageApi;

    public String uploadImage(String base64Image) {
        return imageApi.uploadImage(base64Image);
    }





}

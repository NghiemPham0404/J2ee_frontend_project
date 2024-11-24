package com.example.j2ee_frontend_test.services.apis;

//import com.example.j2ee_frontend_test.response.ImageResponse;
//import retrofit2.Call;
//import retrofit2.http.Multipart;
//import retrofit2.http.POST;
//import retrofit2.http.Part;
//import retrofit2.http.Query;
//
//public interface ImageApi {
//    @Multipart
//    @POST("1/upload")
//    Call<ImageResponse> uploadImage(@Query("image_api_key") String key, @Part Multipart image);
//
//}

import com.example.j2ee_frontend_test.config.ImageBBConfig;
import com.example.j2ee_frontend_test.response.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ImageApi {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ImageBBConfig imageBBConfig;

    public String uploadImage(String base64Image) {
        String apiUrl = imageBBConfig.getBaseUrl() + "?key=" + imageBBConfig.getApiKey();

        // Tạo request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image", base64Image);

        // Gửi POST request
        ImageResponse response = restTemplate.postForObject(apiUrl, requestBody, ImageResponse.class);

        // Xử lý response
        if (response != null && response.getData() != null) {
            System.out.println(response.getImageUrl());
            return response.getImageUrl();
        }

        throw new RuntimeException("Failed to upload image to ImageBB");
    }
}

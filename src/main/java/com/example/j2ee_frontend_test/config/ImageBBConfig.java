package com.example.j2ee_frontend_test.config;

import com.example.j2ee_frontend_test.services.apis.ImageApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class ImageBBConfig {
    @Value("${image.storage.url}")
    String BASE_URL;

    @Value("${image.api.key}")
    String API_KEY;

    public String getBaseUrl() {
        return BASE_URL;
    }

    public String getApiKey() {
        return API_KEY;
    }

//    @Bean("imageRetrofit")
//    public Retrofit getApi() {
//        return new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package com.example.j2ee_frontend_test.config;

import com.example.j2ee_frontend_test.services.AccountApi;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetrofitClientConfig {

    @Value("${base.api.url}")
    String base_url;

    @Bean
    public Retrofit retrofit() {

        return new Retrofit.Builder()
                .baseUrl(base_url)  // Base URL for the backend API
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public AccountApi getAccountApi(Retrofit retrofit) {
        return retrofit.create(AccountApi.class);
    }
}

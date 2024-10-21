package com.example.j2ee_frontend_test.config;

import com.example.j2ee_frontend_test.services.apis.AccountApi;
import com.example.j2ee_frontend_test.services.apis.RoleApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetrofitClientConfig {

    @Value("${base.api.url}")
    String base_url;

    @Bean
    public Retrofit retrofit(Gson gson) {

        return new Retrofit.Builder()
                .baseUrl(base_url)  // Base URL for the backend API
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }


    @Bean
    public AccountApi getAccountApi(Retrofit retrofit) {
        return retrofit.create(AccountApi.class);
    }

    @Bean
    public RoleApi getRoleApi(Retrofit retrofit) {
        return retrofit.create(RoleApi.class);
    }
}

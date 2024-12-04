package com.example.j2ee_frontend_test.config;


import com.example.j2ee_frontend_test.services.apis.*;

import com.example.j2ee_frontend_test.services.apis.AccountApi;
import com.example.j2ee_frontend_test.services.apis.RoleApi;
import com.example.j2ee_frontend_test.services.apis.TransactionStatementApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

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
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setDateFormat("yyyy/MM/dd HH:mm:ss")
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

    @Bean
    public TransactionStatementApi getTransactionStatementApi(Retrofit retrofit) {
        return retrofit.create(TransactionStatementApi.class);
    }


    @Bean
    public PostApi getPostApi(Retrofit retrofit) {
        return retrofit.create(PostApi.class);
    }

    @Bean
    public CharityApi getCharityApi(Retrofit retrofit) {
        return retrofit.create(CharityApi.class);
    }

}

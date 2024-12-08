package com.example.j2ee_frontend_test.config;


import com.example.j2ee_frontend_test.services.apis.*;

import com.example.j2ee_frontend_test.services.apis.AccountApi;
import com.example.j2ee_frontend_test.services.apis.RoleApi;
import com.example.j2ee_frontend_test.services.apis.TransactionStatementApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.LocalDateTime;

@Configuration
public class RetrofitClientConfig {

    @Value("${base.api.url}")
    String base_url;

    private final JwtInterceptor jwtInterceptor;

    private Retrofit retrofit;

    public RetrofitClientConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Bean
    public synchronized Retrofit retrofit(Gson gson) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(jwtInterceptor)// Thêm interceptor
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)  // Base URL for the backend API
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public synchronized void reinitializeRetrofit() {
        retrofit = null; // Clear the existing instance
        retrofit = retrofit(gson()); // Rebuild with the latest context
        System.out.println("Retrofit instance reinitialized with the updated JWT context.");
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy/MM/dd HH:mm:ss")
                .create();
    }

    @Bean
    public LoginApi getLoginApi(Retrofit retrofit) {
        return retrofit.create(LoginApi.class);
    }

    @Bean
    public EmailApi getEmailApi(Retrofit retrofit) {
        return retrofit.create(EmailApi.class);
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
    public PostApi getPostApi(Retrofit retrofit) {
        return retrofit.create(PostApi.class);
    }

    @Bean
    public TransactionStatementApi getTransactionStatementApi(Retrofit retrofit) {
        return retrofit.create(TransactionStatementApi.class);
    }

    @Bean
    public ProfileApi getProfileApi(Retrofit retrofit) {
        return retrofit.create(ProfileApi.class);
    }

    @Bean
    public CharityApi getCharityApi(Retrofit retrofit) {
        return retrofit.create(CharityApi.class);
    }

    @Bean
    public StatisticApi getStatisticApi(Retrofit retrofit) {
        return retrofit.create(StatisticApi.class);
    }

}

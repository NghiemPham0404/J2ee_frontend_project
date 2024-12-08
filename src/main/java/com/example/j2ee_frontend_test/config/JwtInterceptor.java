package com.example.j2ee_frontend_test.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtInterceptor implements Interceptor {
     private final JwtProvider jwtProvider;

     public JwtInterceptor(JwtProvider jwtTokenProvider) {
        this.jwtProvider = jwtTokenProvider;
     }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = jwtProvider.getToken();
        Request.Builder requestBuilder = originalRequest.newBuilder();

        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + token); // Attach the token
        }

        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}

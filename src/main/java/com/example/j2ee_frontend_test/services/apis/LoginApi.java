package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.DTOs.JwtDTO;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {
    @POST("personal-info/login")
    Call<JwtDTO> login(@Query("username") String username, @Query("password") String password);
}

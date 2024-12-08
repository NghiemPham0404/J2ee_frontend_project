package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.DTOs.JwtDTO;
import com.example.j2ee_frontend_test.response.AccountListResponse;
import com.example.j2ee_frontend_test.response.ErrorResponse;
import com.example.j2ee_frontend_test.response.LoginResponse;
import com.example.j2ee_frontend_test.services.apis.LoginApi;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    LoginApi loginApi;

    public LoginResponse Login(String username, String password)  {
        Call<JwtDTO> call = loginApi.login(username, password);
        try {
            Response<JwtDTO> response = call.execute();
            if(response.code() == 200) {
                return new LoginResponse(response.body());
            }else{
                ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class); ;
                System.out.println("loi o service :"+errorResponse.getError());
                return new LoginResponse(errorResponse);
            }
        }catch (Exception exception){
            exception.printStackTrace();
            throw new RuntimeException("Call login thất bại : " + exception.getMessage());
        }
    }
}

package com.example.j2ee_frontend_test.response;

import com.example.j2ee_frontend_test.DTOs.JwtDTO;
import lombok.Getter;

@Getter
public class LoginResponse {
    private JwtDTO data;
    private ErrorResponse error;

    public LoginResponse(JwtDTO data) {
        this.data = data;
    }

    public LoginResponse(ErrorResponse error) {
        this.error = error;
    }

}

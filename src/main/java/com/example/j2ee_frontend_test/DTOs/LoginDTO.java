package com.example.j2ee_frontend_test.DTOs;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;

    private boolean rememberMe;
}

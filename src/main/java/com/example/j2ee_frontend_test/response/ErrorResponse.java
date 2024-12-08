package com.example.j2ee_frontend_test.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private String error;
    private HttpStatus httpStatus;
}

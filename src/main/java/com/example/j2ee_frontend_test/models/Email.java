package com.example.j2ee_frontend_test.models;

//import jakarta.validation.comstraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String toEmail;

    private String subject;

    private String body;
}

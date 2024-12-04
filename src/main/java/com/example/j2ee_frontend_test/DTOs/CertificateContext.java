package com.example.j2ee_frontend_test.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateContext {
    private String fullName;
    private String event;
    private String time;
    private String donation;
}

package com.example.j2ee_frontend_test.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostPostsAccountsDTO {
    int accountId;
    String accountName;
    Long postCount;
}

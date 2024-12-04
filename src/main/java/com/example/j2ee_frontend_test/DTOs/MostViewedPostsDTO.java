package com.example.j2ee_frontend_test.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostViewedPostsDTO {
    private UUID postId;
    private String title;
    private Long count;

}

package com.example.j2ee_frontend_test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostView {

    private Long id;
    private Date time;
    private Post post;
}


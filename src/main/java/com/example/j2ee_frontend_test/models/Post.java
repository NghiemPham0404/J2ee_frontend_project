package com.example.j2ee_frontend_test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private UUID id;

    private String title;

    private String body;

    private Date timePost;

    private String thumbImg;

    private CharityEvent charityEvent;

    private Account account;

    private List<PostView> postViews;

    public void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}


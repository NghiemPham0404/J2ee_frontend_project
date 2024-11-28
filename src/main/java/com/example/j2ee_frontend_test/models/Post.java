package com.example.j2ee_frontend_test.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    private UUID id;

    private String title;

    private String body;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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


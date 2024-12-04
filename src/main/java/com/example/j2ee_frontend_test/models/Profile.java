package com.example.j2ee_frontend_test.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    private Integer id;

    private String name;

    private Date birthDate;

    private String username;

    private String password;

    private String email;

    private boolean active;

    @JsonManagedReference
    private Role role;

    @JsonBackReference
    private List<Post> posts;
}


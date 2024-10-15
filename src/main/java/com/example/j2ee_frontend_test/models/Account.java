package com.example.j2ee_frontend_test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Integer id;

    private String name;

    private Date birthDate;

    private String username;

    private String password;

    private String email;

    private boolean active;

    private Role role;

    public String getBirthDateAsString(String format){
        return new SimpleDateFormat(format).format(birthDate);
    }
}


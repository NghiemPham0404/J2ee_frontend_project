package com.example.j2ee_frontend_test.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Integer id;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private String username;

    private String password;

    private String email;

    private boolean active;

    private Role role;

    private int role_id;

    public String getBirthDateAsString(String format){
        return new SimpleDateFormat(format).format(birthDate);
    }
}


package com.example.j2ee_frontend_test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Integer id;

    private String name;
    private List<RoleAction> roleActions;
}

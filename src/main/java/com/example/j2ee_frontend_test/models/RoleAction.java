package com.example.j2ee_frontend_test.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAction {

    private Integer id;

    private boolean create;

    private boolean update;

    private boolean delete;

    private boolean read;

    private Role role;

    private Action action;
}

package com.example.j2ee_frontend_test.response;

import com.example.j2ee_frontend_test.models.Role;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class RoleListResponse {
    @Expose
    @SerializedName("data")
    private List<Role> roles;
}

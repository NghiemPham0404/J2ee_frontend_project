package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.RoleListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface RoleApi {
     @GET("role/all")
     Call<RoleListResponse> getAllRoles();

     @GET("role/{id}")
     Call<Role> getRoleById(@Path("id") int id);
}

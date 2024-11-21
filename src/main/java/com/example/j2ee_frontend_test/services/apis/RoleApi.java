package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.RoleListResponse;
import org.springframework.http.ResponseEntity;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;
import java.util.UUID;

public interface RoleApi {
     @GET("role/all")
     Call<RoleListResponse> getAllRoles();

     @GET("role/{id}")
     Call<Role> getRoleById(@Path("id") int id);

     @DELETE("role/{id}")
     Call<ResponseEntity<Object>> deleteRole(@Path("id") int id);
}

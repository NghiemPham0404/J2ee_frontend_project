package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.RoleListResponse;
import org.springframework.http.ResponseEntity;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.UUID;

public interface RoleApi {
     @GET("role/all")
     Call<RoleListResponse> getAllRoles();

     @GET("role/{id}")
     Call<Role> getRoleById(@Path("id") int id);

     @DELETE("role/{id}")
     Call<ResponseEntity<Object>> deleteRole(@Path("id") int id);
     @PUT("role/{id}")
     Call<ResponseEntity<Object>> updateRole(@Path("id") int id, @Body Role role);
     @POST("role")
     Call<ResponseEntity<Object>> createRole(@Body Role role);
}

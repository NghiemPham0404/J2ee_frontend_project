package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.RoleListResponse;
import com.example.j2ee_frontend_test.services.apis.RoleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import javax.management.relation.RoleList;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    @Autowired
    RoleApi roleApi;

    public List<Role> getAllRoles() {
        Call<RoleListResponse> call = roleApi.getAllRoles();
        try {
            Response<RoleListResponse> response = call.execute();
            if(response.isSuccessful() && response.body() != null) {
                return response.body().getRoles();
            }else{
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public Role getRoleById(int id) {
         try {
            Response<Role> response = roleApi.getRoleById(id).execute();
            if(response.isSuccessful()) {
                return response.body();
            }else{
                System.out.println("Error: " + response.errorBody());
                return null;
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public String deleteRole(int id) {
        Call<ResponseEntity<Object>> call = roleApi.deleteRole(id);
        try {            Response<ResponseEntity<Object>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String updateRole(int id, Role role) {
        Call<ResponseEntity<Object>> call = roleApi.updateRole(id, role);
        try {
            Response<ResponseEntity<Object>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String createRole(Role role) {
        Call<ResponseEntity<Object>> call = roleApi.createRole(role);
        try {
            Response<ResponseEntity<Object>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.Profile;
import com.example.j2ee_frontend_test.services.apis.ProfileApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Service
public class ProfileService {

    @Autowired
    private ProfileApi profileApi;

    public Profile getProfileSessionsByEvent(int id) {
        Call<Profile> call = profileApi.getProfileById(id);
        Response<Profile> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody());
                return new Profile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Profile findById(int id) {
        Call<Profile> call = profileApi.getProfileById(id);
        try {
            Response<Profile> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody());
                return new Profile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


//    public String updateProfile(UUID id, Profile profile) {
////        Call<ResponseEntity<Object>> call = ProfileApi.updateProfile(id, profile);
//        try {
//            Response<ResponseEntity<Object>> response = call.execute();
//            if (response.isSuccessful() && response.body() != null) {
//                System.out.println("Success");
//                return response.body().toString();
//            } else {
//                System.out.println("Error: " + response.errorBody().string());
//                return null;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Account getPersonalInfo() {
        Call<Account> call = profileApi.getPersonalInfo();
        try {
            Response<Account> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody());
                return new Account();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


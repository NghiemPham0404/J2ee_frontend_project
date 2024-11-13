package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.Profile;
import com.example.j2ee_frontend_test.response.ProfileListResponse;
import com.example.j2ee_frontend_test.response.TransferSessionListResponse;
import com.example.j2ee_frontend_test.services.apis.ProfileApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProfileService {

    @Autowired
    ProfileApi profileApi;
    public ProfileListResponse getProfileSessionsByEvent(String eventId, int page) {
        Call<ProfileListResponse> call = ProfileApi.getProfileByEvent(eventId, page);
        Response<ProfileListResponse> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody());
                return new ProfileListResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Profile findById(long l) {
        return null;
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
}


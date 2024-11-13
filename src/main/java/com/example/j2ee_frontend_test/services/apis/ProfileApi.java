package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.Profile;
import com.example.j2ee_frontend_test.response.ProfileListResponse;
import org.springframework.http.ResponseEntity;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProfileApi {

    static Call<ProfileListResponse> getProfileByEvent(String eventId, int page) {
        return null;
    }

    @GET("personal-info")
    Call<ProfileListResponse> getAllAccounts(@Query("admin_id")int adminId, @Query("page") int page);

    @GET("personal-info/{id}")
    Call<Profile> getProfileById(@Path("id") Integer id);

//    static Call<ResponseEntity<Object>> updateProfile(@Path("id") Integer id, @Body Profile profile);

}

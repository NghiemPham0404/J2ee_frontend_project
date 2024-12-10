package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.Profile;
import com.example.j2ee_frontend_test.response.ProfileListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProfileApi {

    @GET("personal-info")
    Call<ProfileListResponse> getAllAccounts(@Query("admin_id")int adminId, @Query("page") int page);

    @GET("personal-info/{id}")
    Call<Profile> getProfileById(@Path("id") Integer id);

    @GET("personal-info/validateAdmin")
    Call<Profile> validateAdmin(@Query("username") String username);


}

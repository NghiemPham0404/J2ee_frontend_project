package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.response.CharityListResponse;
import org.springframework.http.ResponseEntity;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.UUID;
public interface CharityApi {
    @GET("charity-events")
    Call<CharityListResponse> getAllCharities(@Query("admin_id")int adminId, @Query("page") int page);

    @POST("charity-events")
    Call<ResponseEntity<Object>> createCharity(@Body CharityEvent charity);

    @GET("charity-events/{id}")
    Call<CharityEvent> getCharityById(@Path("id") UUID id);

    @PUT("charity/{id}")
    Call<ResponseEntity<Object>> updateCharity(@Path("id") UUID id, @Body CharityEvent charity);

    @DELETE("charity/{id}")
    Call<ResponseEntity<Object>> deleteCharity(@Path("id") UUID id);
}

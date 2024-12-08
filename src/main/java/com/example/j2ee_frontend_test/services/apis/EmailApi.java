package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.DTOs.CertificationDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EmailApi {
    @POST("email/certification")
    Call<Void> getCertification(@Query("transferId") String transferId, @Query("recipientAddress") String email);
}

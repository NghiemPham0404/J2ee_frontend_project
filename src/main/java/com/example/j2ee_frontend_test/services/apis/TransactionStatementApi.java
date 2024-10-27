package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.response.TransferSessionListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface TransactionStatementApi {
    @GET("charity-event/{eventId}/transfer-sessions")
    Call<TransferSessionListResponse> getTransferSessionsByEvent(
            @Path("eventId") String eventId,
            @Query("page") int page
    );
}

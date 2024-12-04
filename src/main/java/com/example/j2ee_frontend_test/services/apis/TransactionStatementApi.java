package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.TransferSession;
import com.example.j2ee_frontend_test.response.MessageResponse;
import com.example.j2ee_frontend_test.response.TransferSessionListResponse;
import retrofit2.Call;
import retrofit2.http.*;


public interface TransactionStatementApi {
    @GET("charity-events/{eventId}/all-transfer")
    Call<TransferSessionListResponse> getTransferSessionsByEvent(
            @Path("eventId") String eventId
    );

    @POST("charity-events/transfer")
    Call<MessageResponse> recordTranferSession(@Query("id") String eventId, @Body TransferSession transferSession);
}

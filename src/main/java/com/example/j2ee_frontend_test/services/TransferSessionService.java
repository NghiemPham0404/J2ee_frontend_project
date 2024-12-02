package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.TransferSession;
import com.example.j2ee_frontend_test.response.TransferSessionListResponse;
import com.example.j2ee_frontend_test.services.apis.TransactionStatementApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Service
public class TransferSessionService {

    @Autowired
    TransactionStatementApi transactionStatementApi;


    public TransferSessionListResponse getTransferSessionsByEvent(String eventId) {
        Call<TransferSessionListResponse> call = transactionStatementApi.getTransferSessionsByEvent(eventId);

        Response<TransferSessionListResponse> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody());
                return new TransferSessionListResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

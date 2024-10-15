package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.response.AccountResponse;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface AccountApi {

    @GET("account/all")
    Call<AccountResponse> getAllAccounts(@Query("admin_id")int adminId, @Query("page") int page);

    @POST("account")
    Call<String> createAccount(@Body Account account);

    @GET("account/{id}")
    Call<Account> getAccountById(@Path("id") Integer id);

    @PUT("account/{id}")
    Call<String> updateAccount(@Path("id") Integer id, @Body Account account);

    @DELETE("account/{id}")
    Call<Void> deleteAccount(@Path("id") Integer id);
}

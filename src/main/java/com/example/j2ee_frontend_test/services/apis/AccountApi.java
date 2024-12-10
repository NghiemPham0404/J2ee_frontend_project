package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.response.AccountListResponse;
import org.springframework.http.ResponseEntity;
import retrofit2.Call;
import retrofit2.http.*;

public interface AccountApi {

    @GET("account/all")
    Call<AccountListResponse> getAllAccounts(@Query("admin_id")int adminId, @Query("page") int page);

    @POST("account")
    Call<ResponseEntity<Object>> createAccount(@Body Account account);

    @GET("account/{id}")
    Call<Account> getAccountById(@Path("id") Integer id);

    @PUT("account/{id}")
    Call<ResponseEntity<Object>> updateAccount(@Path("id") Integer id, @Body Account account);

    @DELETE("account/{id}")
    Call<ResponseEntity<Object>> deleteAccount(@Path("id") Integer id);

    @GET("account/search")
    Call<AccountListResponse> searchNameAccounts(@Query("query") String query, @Query("page") int page);

}

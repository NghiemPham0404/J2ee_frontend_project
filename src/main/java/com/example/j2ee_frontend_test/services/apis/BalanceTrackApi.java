package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.response.BalanceTrackingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceTrackApi {
    @GET("balance-tracking")
    Call<BalanceTrackingResponse> balanceTracking(@Query("year") int year);

    @GET("all-active-year")
    Call<List<Integer>> getAllActiveYear();

    @GET("remain-balance")
    Call<BigDecimal> getRemainBalance();
}

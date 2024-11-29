package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.response.MostPostAccountListResponse;
import com.example.j2ee_frontend_test.response.PostListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Date;

public interface StatisticApi {
    @GET("statistic/account/most-posts-accounts")
    Call<MostPostAccountListResponse> getMostPostAccount(@Query("startDate") Date startDate, @Query("endDate") Date endDate);
}

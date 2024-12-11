package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.response.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Date;
import java.util.UUID;

public interface StatisticApi {
    @GET("statistic/account/most-posts-accounts")
    Call<MostPostAccountListResponse> getMostPostAccount(@Query("startTime") String startDate, @Query("endTime") String endDate);
    @GET("statistic/charity-event/most-donation-events")
    Call<MostDonationEventListResponse> getMostDonationEvent(@Query("startTime") String startDate, @Query("endTime") String endDate);
    @GET("statistic/post/most-viewed-posts")
    Call<MostViewedPostListResponse> getMostViewedPost(@Query("startTime") String startDate, @Query("endTime") String endDate);
    @GET("statistic/charity-event/{ce_id}/most-charitatble-person")
    Call<MostCharitableListResponse> getMostCharitable(@Path("ce_id") UUID id, @Query("startTime") String startDate, @Query("endTime") String endDate);
    @GET("statistic/charity-event/disburse")
    Call<CharityListResponse> getDisburse(@Query("startTime") String startDate,@Query("endTime") String endDate);
    @GET("statistic/charity-event/{ce_id}/top-10")
    Call<TransferSessionListResponse> getTop10(@Path("ce_id") UUID id);
}

package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.response.*;
import com.example.j2ee_frontend_test.services.apis.StatisticApi;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class StatisticService {
   @Autowired
    StatisticApi statisticApi;

    public MostPostAccountListResponse getMostPostAccount(Date startDate, Date endDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Call<MostPostAccountListResponse> call = statisticApi.getMostPostAccount(df.format(startDate), df.format(endDate));
        try {
            Response<MostPostAccountListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                if (response.errorBody() != null) {
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorDetails = errorBody.string();
                        System.out.println("Error: " + errorDetails);
                    }
                } else {
                    System.out.println("Error: No error body provided.");
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MostDonationEventListResponse getMostDonationEvent(Date startDate, Date endDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Call<MostDonationEventListResponse> call = statisticApi.getMostDonationEvent (df.format(startDate), df.format(endDate));
        try {
            Response<MostDonationEventListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                if (response.errorBody() != null) {
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorDetails = errorBody.string();
                        System.out.println("Error: " + errorDetails);
                    }
                } else {
                    System.out.println("Error: No error body provided.");
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MostViewedPostListResponse getMostViewedPosts(Date startDate, Date endDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Call<MostViewedPostListResponse> call = statisticApi.getMostViewedPost(df.format(startDate), df.format(endDate));
        try {
            Response<MostViewedPostListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                if (response.errorBody() != null) {
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorDetails = errorBody.string();
                        System.out.println("Error: " + errorDetails);
                    }
                } else {
                    System.out.println("Error: No error body provided.");
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public MostCharitableListResponse getMostCharitable(UUID id, Date startDate, Date endDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Call<MostCharitableListResponse> call = statisticApi.getMostCharitable(id,df.format(startDate), df.format(endDate));
        try {
            Response<MostCharitableListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                if (response.errorBody() != null) {
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorDetails = errorBody.string();
                        System.out.println("Error: " + errorDetails);
                    }
                } else {
                    System.out.println("Error: No error body provided.");
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public CharityListResponse disburseCharity(Date startDate, Date endDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Call<CharityListResponse> call = statisticApi.getDisburse(df.format(startDate), df.format(endDate));
        try {
            Response<CharityListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                if (response.errorBody() != null) {
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorDetails = errorBody.string();
                        System.out.println("Error: " + errorDetails);
                    }
                } else {
                    System.out.println("Error: No error body provided.");
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public TransferSessionListResponse top10(UUID id) {
       Call<TransferSessionListResponse> call=statisticApi.getTop10(id);
        try {
            Response<TransferSessionListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                if (response.errorBody() != null) {
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorDetails = errorBody.string();
                        System.out.println("Error: " + errorDetails);
                    }
                } else {
                    System.out.println("Error: No error body provided.");
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

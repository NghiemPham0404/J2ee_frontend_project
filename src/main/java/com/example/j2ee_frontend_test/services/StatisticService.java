package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.MostPostAccount;
import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.MostPostAccountListResponse;
import com.example.j2ee_frontend_test.response.RoleListResponse;
import com.example.j2ee_frontend_test.services.apis.StatisticApi;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class StatisticService {
   @Autowired
    StatisticApi statisticApi;

    public MostPostAccountListResponse getMostPostAccount(Date startDate, Date endDate) {
        Call<MostPostAccountListResponse> call = statisticApi.getMostPostAccount(startDate, endDate);
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

}

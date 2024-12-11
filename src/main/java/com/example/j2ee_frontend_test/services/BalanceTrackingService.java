package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.DTOs.BalanceTrackingDTO;
import com.example.j2ee_frontend_test.response.BalanceTrackingResponse;
import com.example.j2ee_frontend_test.services.apis.BalanceTrackApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Path;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BalanceTrackingService {

    @Autowired
    private BalanceTrackApi balanceTrackApi;

    public List<BalanceTrackingDTO> balanceTracking(int year){
        Call<BalanceTrackingResponse> balanceTrackingResponseCall = balanceTrackApi.balanceTracking(year);
        try {
            Response<BalanceTrackingResponse> balanceTrackingResponseResponse = balanceTrackingResponseCall.execute();
            if(balanceTrackingResponseResponse.isSuccessful()){
                BalanceTrackingResponse balanceTrackingResponse = balanceTrackingResponseResponse.body();
                if(balanceTrackingResponse != null){
                    return balanceTrackingResponse.getData();
                }else{
                    return new ArrayList<BalanceTrackingDTO>();
                }
            }else{
                throw new RuntimeException("" + balanceTrackingResponseResponse.errorBody().string());
            }
        }catch (IOException e){
            e.printStackTrace();
            return new ArrayList<BalanceTrackingDTO>();
        }
    }

    public List<Integer> getAllActiveYear(){
        Call<List<Integer>> AllActiveYearApiCall = balanceTrackApi.getAllActiveYear();
        try {
            Response<List<Integer>> AllActiveYearResponse = AllActiveYearApiCall.execute();
            if(AllActiveYearResponse.isSuccessful()){
                List<Integer> balanceTrackingResponse = AllActiveYearResponse.body();
                return Objects.requireNonNullElseGet(balanceTrackingResponse, ArrayList::new);
            }else{
                throw new RuntimeException("" + AllActiveYearResponse.errorBody().string());
            }
        }catch (IOException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public BigDecimal getRemainBalance(){
        Call<BigDecimal> remainBalanceCall = balanceTrackApi.getRemainBalance();
        try {
            Response<BigDecimal> remainBalanceResponse = remainBalanceCall.execute();
            if(remainBalanceResponse.isSuccessful()){
                BigDecimal remainBalance = remainBalanceResponse.body();
                return remainBalance!=null?remainBalance:BigDecimal.ZERO;
            }else{
                throw new RuntimeException("" + remainBalanceResponse.errorBody().string());
            }
        }catch (IOException e){
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}

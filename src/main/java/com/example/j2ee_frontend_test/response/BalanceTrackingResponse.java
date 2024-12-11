package com.example.j2ee_frontend_test.response;

import com.example.j2ee_frontend_test.DTOs.BalanceTrackingDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class BalanceTrackingResponse {
    @Expose
    @SerializedName("data")
    private List<BalanceTrackingDTO> data;
}

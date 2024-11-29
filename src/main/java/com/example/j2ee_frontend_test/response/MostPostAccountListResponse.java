package com.example.j2ee_frontend_test.response;

import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.models.MostPostAccount;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MostPostAccountListResponse {
    @Expose
    @SerializedName("data")
    private List<MostPostAccount> mostPostAccountList;

}

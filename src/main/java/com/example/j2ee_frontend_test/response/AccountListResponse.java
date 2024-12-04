package com.example.j2ee_frontend_test.response;

import com.example.j2ee_frontend_test.models.Account;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class AccountListResponse {
    @Expose
    @SerializedName("data")
    private List<Account> accountList;

    @Expose
    @SerializedName("total_results")
    private int totalResults;

    @Expose
    @SerializedName("total_page")
    private int totalPages;
}

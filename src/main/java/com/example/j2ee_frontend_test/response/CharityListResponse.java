package com.example.j2ee_frontend_test.response;

import com.example.j2ee_frontend_test.models.CharityEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;
    @Getter
    public class CharityListResponse {
        @Expose
        @SerializedName("data")
        private List<CharityEvent> charityList;

        @Expose
        @SerializedName("total_results")
        private int totalResults;

        @Expose
        @SerializedName("total_pages")
        private int totalPages;
    }


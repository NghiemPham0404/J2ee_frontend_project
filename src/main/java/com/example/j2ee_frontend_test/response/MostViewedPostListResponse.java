package com.example.j2ee_frontend_test.response;


import com.example.j2ee_frontend_test.DTOs.MostViewedPostsDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
@Data
public class MostViewedPostListResponse {
    @Expose
    @SerializedName("data")
    private List<MostViewedPostsDTO> mostViewedPostList;
}

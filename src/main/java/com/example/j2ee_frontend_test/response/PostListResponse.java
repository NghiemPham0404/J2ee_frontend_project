package com.example.j2ee_frontend_test.response;

import com.example.j2ee_frontend_test.models.Post;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponse {
    @Expose
    @SerializedName("data")
    private List<Post> postList;

    @Expose
    @SerializedName("dataNotApproved")
    private List<Post> postListNotApproved;

    @Expose
    @SerializedName("total_results")
    private int totalResults;

    @Expose
    @SerializedName("total_pages")
    private int totalPages;
}

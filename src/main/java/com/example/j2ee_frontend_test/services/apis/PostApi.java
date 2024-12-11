package com.example.j2ee_frontend_test.services.apis;

import com.example.j2ee_frontend_test.models.Post;
import com.example.j2ee_frontend_test.response.PostListResponse;
import org.springframework.http.ResponseEntity;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.UUID;

public interface PostApi {
    @GET("posts/all")
    Call<PostListResponse> getAllPosts(@Query("admin_id")int adminId, @Query("page") int page);

    @GET("posts/my-posts")
    Call<PostListResponse> getMyPosts(@Query("page") int page, @Query("ownerId") int ownerId);

    @POST("posts")
    Call<ResponseEntity<Object>> createPost(@Body Post post);

    @GET("posts/{id}")
    Call<Post> getPostById(@Path("id") UUID id);

    @GET("posts/approved/{id}")
    Call<Post> getPostByIdForUser(@Path("id") UUID id);

    @GET("posts/not-approved")
    Call<PostListResponse> getNotApprovedPosts(@Query("page") int page);

    @POST("posts/post/{id}/approved")
    Call<ResponseEntity<Object>> approvePost(@Path("id") UUID id);

    @PUT("posts/{id}")
    Call<ResponseEntity<Object>> updatePost(@Path("id") UUID id, @Body Post post);

    @DELETE("posts/{id}")
    Call<ResponseEntity<Object>> deletePost(@Path("id") UUID id);

    @GET("posts/search-all")
    Call<PostListResponse> searchPostsByTitle(@Query("page") int page, @Query("query") String query);

    @GET("posts/search/my-posts")
    Call<PostListResponse> searchMyPostsByTitle(@Query("page") int page, @Query("query") String query, @Query("ownerId") int ownerId);
    @GET("posts")
    Call<PostListResponse> getAllPostsforUser(@Query("page") int page);

    @GET("posts/search")
    Call<PostListResponse> searchPosts(@Query("page") int page,@Query("query") String query);
}

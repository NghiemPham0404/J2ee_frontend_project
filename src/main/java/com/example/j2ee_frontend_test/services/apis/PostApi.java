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

    @POST("posts")
    Call<ResponseEntity<Object>> createPost(@Body Post post);

    @GET("posts/{id}")
    Call<Post> getPostById(@Path("id") UUID id);

    @PUT("posts/{id}")
    Call<ResponseEntity<Object>> updatePost(@Path("id") UUID id, @Body Post post);

    @DELETE("posts/{id}")
    Call<ResponseEntity<Object>> deletePost(@Path("id") UUID id);

    @GET("posts/search-all")
    Call<PostListResponse> searchPostsByTitle(@Query("admin_id") int adminId, @Query("page") int page , @Query("title") String title);
}

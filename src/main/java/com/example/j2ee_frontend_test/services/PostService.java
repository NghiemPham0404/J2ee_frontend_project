package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.Post;
import com.example.j2ee_frontend_test.response.PostListResponse;
import com.example.j2ee_frontend_test.services.apis.PostApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.UUID;

@Service
public class PostService {
    @Autowired
    PostApi postApi;

    public PostListResponse getAllPosts(int adminId, int page) {
        Call<PostListResponse> call = postApi.getAllPosts(adminId, page);
        Response<PostListResponse> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                // Handle errors here, if needed
                System.out.println("Error: " + response.errorBody());
                return new PostListResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PostListResponse getAllPostsforuser(int page) {
        Call<PostListResponse> call = postApi.getAllPostsforUser( page);
        Response<PostListResponse> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                // Handle errors here, if needed
                System.out.println("Error: " + response.errorBody());
                return new PostListResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createPost(Post post) {
        Call<ResponseEntity<Object>> call = postApi.createPost(post);
        try {
            Response<ResponseEntity<Object>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Post getPostById(UUID id) {
        Call<Post> call = postApi.getPostById(id);
        try {
            Response<Post> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String updatePost(UUID id, Post post) {
        Call<ResponseEntity<Object>> call = postApi.updatePost(id, post);
        try {
            Response<ResponseEntity<Object>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String deletePost(UUID id) {
        Call<ResponseEntity<Object>> call = postApi.deletePost(id);
        try {
            Response<ResponseEntity<Object>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

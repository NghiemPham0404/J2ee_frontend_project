package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.Post;
import com.example.j2ee_frontend_test.response.PostListResponse;
import com.example.j2ee_frontend_test.services.PostService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping
    public String viewPostsPage(Model model, @PathParam("page") Integer page) {
        page = page != null ? page - 1: 0;
        PostListResponse postListResponse = postService.getAllPosts(1, page);
        model.addAttribute("data", postListResponse.getPostList());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", postListResponse.getTotalPages());
        model.addAttribute("total_results", postListResponse.getTotalResults());

        return "post";
    }

    @GetMapping("/new")
    public String showNewPostPage(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "create_post";
    }
}


package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostController {

    @GetMapping
    public String showPostManagerPage(Model model) {
        return "post"; // Trả về tên trang HTML (postManager.html)
    }

    @GetMapping("/new")
    public String showNewPostPage(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "create_post";
    }
}


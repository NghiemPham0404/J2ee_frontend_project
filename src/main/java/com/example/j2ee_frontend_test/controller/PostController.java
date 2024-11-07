package com.example.j2ee_frontend_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    @GetMapping("/posts")
    public String showPostManagerPage(Model model) {
        return "post"; // Trả về tên trang HTML (postManager.html)
    }
}


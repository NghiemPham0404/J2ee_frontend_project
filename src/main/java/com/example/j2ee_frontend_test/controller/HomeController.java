package com.example.j2ee_frontend_test.controller;
import com.example.j2ee_frontend_test.response.PostListResponse;
import com.example.j2ee_frontend_test.services.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    PostService postService;

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) throws JsonProcessingException {
        model.addAttribute("currentUri", request.getRequestURI());
        int page=0;
        PostListResponse postListResponse = postService.getAllPostsforuser(0);

        System.out.println(postListResponse.getPostList());
        ObjectMapper objectMapper = new ObjectMapper();
        String charityJson = objectMapper.writeValueAsString(postListResponse);
        System.out.println(charityJson);
        model.addAttribute("data", postListResponse.getPostList());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", postListResponse.getTotalPages());
        model.addAttribute("total_results", postListResponse.getTotalResults());
        return "home";
    }
}

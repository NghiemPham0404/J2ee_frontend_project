package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.models.Post;
import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.PostListResponse;
import com.example.j2ee_frontend_test.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CharityService charityService;


    @GetMapping
    public String viewPostsPage(Model model, @PathParam("page") Integer page, @PathParam("query") String query) {
        page = page != null ? page - 1: 0;
        PostListResponse postListResponse = null;

        if (query != null && !query.isEmpty()) {
            postListResponse = postService.getMyPosts(page, 1, query);

            if (postListResponse == null) {
                model.addAttribute("message", "Không tìm thấy bài viết nào!");
                model.addAttribute("data", new ArrayList<>());
                model.addAttribute("page", 0);
                model.addAttribute("total_pages", 0);
                model.addAttribute("total_results", 0);
                model.addAttribute("query", query);
                return "post";
            }

            // Kiểm tra nếu có bài viết nào có title khớp với query
//            boolean isQueryMatch = postListResponse.getPostList().stream()
//                    .anyMatch(post -> post.getTitle().toLowerCase().contains(query.toLowerCase())); // Kiểm tra khớp title (không phân biệt chữ hoa chữ thường)
//
//            // Nếu không có bài viết khớp, trả về thông báo lỗi
//            if (!isQueryMatch) {
//                model.addAttribute("message", "No posts found matching your query.");
//                model.addAttribute("data", new ArrayList<>());
//                model.addAttribute("page", 0);
//                model.addAttribute("total_pages", 0);
//                model.addAttribute("total_results", 0);
//                model.addAttribute("query", query);
//                return "post";
//
//            }
        }
        else {
            postListResponse = postService.getMyPosts(page, 1, "");

        }

        model.addAttribute("data", postListResponse.getPostList());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", postListResponse.getTotalPages());
        model.addAttribute("total_results", postListResponse.getTotalResults());
        model.addAttribute("query", query);


        //System.out.println("Response:" +postListResponse.getPostList());

        return "post";
    }

    @GetMapping("/new")
    public String showNewPostPage(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        model.addAttribute("charityEvents", charityService.getAllCharities(0).getCharityList());

        return "create_post";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute("post") Post post) throws Exception {
        if (post.getAccount() != null && post.getAccount().getId() != null) {
            System.out.println("Account ID:" + post.getAccount().getId());
            Account account = accountService.getAccountById(post.getAccount().getId());
            post.setAccount(account);
        }
        else {
            System.out.println("Account ID is null");
        }


        if (post.getCharityEvent() != null && post.getCharityEvent().getId() != null) {
            CharityEvent charityEvent = charityService.getCharityById(post.getCharityEvent().getId());
            post.setCharityEvent(charityEvent);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        String jString = objectMapper.writeValueAsString(post);
        System.out.println(jString);

        if (post.getId() != null) {
            postService.updatePost(post.getId(), post);
        } else {

            postService.createPost(post);
        }


        return "redirect:/posts";
    }

    @GetMapping("/update/{id}")
    public String showUpdatePostPage(Model model, @PathVariable("id") UUID id) {
        Post post = postService.getPostById(id);
        System.out.println(post);
        model.addAttribute("post", post);
        model.addAttribute("charityEvents", charityService.getAllCharities(0).getCharityList());
        return "detail_post";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") UUID id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }



}


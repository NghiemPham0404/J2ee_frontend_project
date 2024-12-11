package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.config.JwtProvider;
import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.models.Post;
import com.example.j2ee_frontend_test.models.Profile;
import com.example.j2ee_frontend_test.response.PostListResponse;
import com.example.j2ee_frontend_test.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping("/posts")
@PreAuthorize("hasAuthority('Post Management read')")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    private ProfileService profileService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CharityService charityService;
    @Autowired
    private JwtProvider jwtProvider;


    @GetMapping
    public String viewPostsPage(Model model, @PathParam("page") Integer page, @PathParam("query") String query) {
        page = page != null ? page - 1 : 0;
        PostListResponse postListResponse = null;
        PostListResponse postListResponseForAdmin = null;

        Account personalInfo = profileService.getPersonalInfo();
        System.out.println("Personal Info: " + personalInfo.getId());

        // lay id cua nguoi dung da dang nhap
        int ownerId = personalInfo.getId();

        boolean isAdmin = false;

        // kiem tra xem co phai admin khong
        if (ownerId == 1) {
            isAdmin = true;
            System.out.println("Yes, this is admin");
        }


        if (query != null && !query.isEmpty()) {
            postListResponse = postService.getMyPosts(page, ownerId, query);
            postListResponseForAdmin = postService.getAllPosts(1, page, query);

            if (postListResponse == null || postListResponse.getPostList().isEmpty() && postListResponseForAdmin == null || postListResponseForAdmin.getPostList().isEmpty()) {
                model.addAttribute("message", "Không tìm thấy bài viết nào!");
                model.addAttribute("data", new ArrayList<>());
                model.addAttribute("dataForAdmin", new ArrayList<>());
                model.addAttribute("page", 0);
                model.addAttribute("total_pages", 0);
                model.addAttribute("total_results", 0);
                model.addAttribute("query", query);
                model.addAttribute("queryForAdmin", query);
                model.addAttribute("isAdmin", isAdmin);
                return "post";
            }

        } else {
            postListResponse = postService.getMyPosts(page, ownerId, "");
            postListResponseForAdmin = postService.getAllPosts(1, page, "");
            PostListResponse postListResponseNotApproved = postService.getNotApprovedPosts(page);


            if (postListResponseNotApproved != null && postListResponseNotApproved.getPostList() != null) {
                model.addAttribute("dataNotApproved", postListResponseNotApproved.getPostList());
            } else {
                model.addAttribute("dataNotApproved", new ArrayList<>());
                model.addAttribute("message_not_approved", "Không có bài viết nào để duyệt!");
            }


        }

        System.out.println("PostListResponse: " + postListResponseForAdmin.getPostList());

        if (postListResponse != null) {
            model.addAttribute("data", postListResponse.getPostList());
            model.addAttribute("total_pages", postListResponse.getTotalPages());
            model.addAttribute("total_results", postListResponse.getTotalResults());
        }else{
             model.addAttribute("data", new ArrayList<>());
            model.addAttribute("total_pages", 0);
            model.addAttribute("total_results",0);
        }

        model.addAttribute("dataForAdmin", postListResponseForAdmin.getPostList());
        model.addAttribute("page", page);

        model.addAttribute("query", query);
        model.addAttribute("queryForAdmin", query);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("create", jwtProvider.containAuthority("Post Management create"));
        return "post";
    }

    @GetMapping("/new")
    public String showNewPostPage(Model model) {
        Post post = new Post();
        Account personalInfo = profileService.getPersonalInfo();
        int ownerId = personalInfo.getId();
        model.addAttribute("post", post);
        model.addAttribute("charityEvents", charityService.getCharityEventsWithoutPost(0).getCharityList());
        model.addAttribute("ownerId", ownerId);
        return "create_post";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute("post") Post post) throws Exception {
        if (post.getAccount() == null) {
            Account account = profileService.getPersonalInfo();
            post.setAccount(account);
            System.out.println("Account is null");
        } else {
            System.out.println("Account ID is null");
        }


        if (post.getCharityEvent() != null && post.getCharityEvent().getId() != null) {
            CharityEvent charityEvent = charityService.getCharityById(post.getCharityEvent().getId());
            post.setCharityEvent(charityEvent);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        String jString = objectMapper.writeValueAsString(post);
        System.out.println(jString); // luc nay la no ch tao id cho post, luu r no tu dong tao

        if (post.getId() != null) {
            postService.updatePost(post.getId(), post);
        } else {

            postService.createPost(post);
        }
        return "redirect:/posts";
    }

    @GetMapping("/update/{id}")
    public String showUpdatePostPage(Model model, @PathVariable("id") UUID id) {

        Account personalInfo = profileService.getPersonalInfo();
        int ownerId = personalInfo.getId();
        System.out.println("Owner ID: " + ownerId);

        boolean isAdmin = false;

        // kiem tra xem co phai admin khong
        if (ownerId == 1) {
            isAdmin = true;
            System.out.println("Yes, this is admin");
        }

        Post post = postService.getPostById(id);
        System.out.println(post);
        model.addAttribute("post", post);
        model.addAttribute("charityEvents", charityService.getAllCharities(0).getCharityList());
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("update", jwtProvider.containAuthority("Post Management update"));
        model.addAttribute("delete", jwtProvider.containAuthority("Post Management delete"));
        return "detail_post";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") UUID id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @GetMapping("/approved/{id}")
    public String approvePost(@PathVariable("id") UUID id) {
        postService.approvePost(id);
        return "redirect:/posts";
    }


}


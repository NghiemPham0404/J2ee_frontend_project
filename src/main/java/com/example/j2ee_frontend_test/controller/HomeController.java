package com.example.j2ee_frontend_test.controller;
import com.example.j2ee_frontend_test.models.Post;
import com.example.j2ee_frontend_test.response.PostListResponse;
import com.example.j2ee_frontend_test.services.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    PostService postService;

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) throws JsonProcessingException {
        model.addAttribute("currentUri", request.getRequestURI());
        int totalPages=postService.getAllPostsforuser(0).getTotalPages();

        List<Post> allPost= new ArrayList<>();
        for (int page=0;page<totalPages;page++){
            PostListResponse postListResponse=postService.getAllPostsforuser(page);
            allPost.addAll(postListResponse.getPostList());
        }
        Post post=postService.getPostByIdForUser(UUID.fromString("6eb66d3f-48bb-4d76-8eae-993d5a2d10b0"));
        System.out.println("bài post tên : " + post.getTitle());
        timeLeft(post);
        PostListResponse postListResponse=postService.getAllPostsforuser(0);
        List<Post> data=postListResponse.getPostList();
        List<Post> done= data.stream().filter(p-> p.getCharityEvent().getIsDisbursed()!=null).collect(Collectors.toList());
        System.out.println("Done:" + done);

        for (Post p : data) {
            timeLeft(p);
        }

        model.addAttribute("ip",post);
        model.addAttribute("data", data);
        model.addAttribute("done", done);
        model.addAttribute("total_pages", totalPages);
        return "home";
    }
    @GetMapping("/contact")
    public String introduce(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "contact";
    }
    @GetMapping("/charities_events")
    public String charities_events(Model model,@PathParam("page") Integer page,@PathParam("query") String query){
        page = page != null ? page : 0;
        PostListResponse postListResponse;
        List<Post> data=new ArrayList<>();
        int pages=postService.getAllPostsforuser(0).getTotalPages();

        if (query == null) {
            for (int i=0;i<=pages;i++){
                postListResponse=postService.getAllPostsforuser(i);
                data.addAll(postListResponse.getPostList());
            }
            for (Post p : data) {
                timeLeft(p);
            }
            model.addAttribute("total_pages",pages);
            model.addAttribute("data", data);
        } else {
            for (int i=0;i<=pages;i++){
                postListResponse=postService.searchPosts(i,query);
                data.addAll(postListResponse.getPostList());
            }
            for (Post p : data) {
                timeLeft(p);
            }
            model.addAttribute("total_pages",pages);
            model.addAttribute("data", data);
        }
        model.addAttribute("query",query);
        model.addAttribute("page", page);
        return "charities";
    }

    public void timeLeft(Post p) {
        Date endDate = p.getCharityEvent().getEndTime();  // Giả sử endTime là kiểu Date
        LocalDateTime now = LocalDateTime.now();

        // Chuyển Date thành LocalDateTime
        LocalDateTime endTime = endDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Tính toán thời gian còn lại
        if (now.isBefore(endTime)) {
            Duration duration = Duration.between(now, endTime);
            long daysLeft = duration.toDays();
            long hoursLeft = duration.minusDays(daysLeft).toHours();
            long minutesLeft = duration.minusDays(daysLeft).minusHours(hoursLeft).toMinutes();
            long secondsLeft = duration.minusDays(daysLeft).minusHours(hoursLeft).minusMinutes(minutesLeft).getSeconds();

            String timeLeft = String.format("%d : %d : %d : %d", daysLeft, hoursLeft, minutesLeft, secondsLeft);
            p.getCharityEvent().setTimeLeft(timeLeft); // Set time left to CharityEvent
        } else {
            p.getCharityEvent().setTimeLeft("Dự án đã kết thúc.");
        }
    }

    @GetMapping("/article/{id}")
    public String article (Model model, HttpServletRequest request, @PathVariable("id") String id) throws JsonProcessingException {
        model.addAttribute("currentUri", request.getRequestURI());

        Post post=postService.getPostByIdForUser(UUID.fromString(id));
        timeLeft(post);
        PostListResponse postListResponse=postService.getAllPostsforuser(0);
        List<Post> data=postListResponse.getPostList();
        List<Post> done= data.stream().filter(p-> p.getCharityEvent().getIsDisbursed()!=null).collect(Collectors.toList());
        System.out.println("Done:" + done);

        for (Post p : data) {
            timeLeft(p);
        }

        model.addAttribute("ip",post);
        model.addAttribute("data", data);
        model.addAttribute("done", done);

        return "article";
    }
}

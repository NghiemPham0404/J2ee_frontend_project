package com.example.j2ee_frontend_test.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        // Thêm dữ liệu động nếu cần thiết
        model.addAttribute("title", "Trang Chủ - Dự Án Từ Thiện");

        // Trả về tên của template Thymeleaf (không cần thêm .html)
        return "home"; // Tên của file Thymeleaf là home.html
    }

}

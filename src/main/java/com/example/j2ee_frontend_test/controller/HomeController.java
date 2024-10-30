package com.example.j2ee_frontend_test.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "home";
    }

}

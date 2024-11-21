package com.example.j2ee_frontend_test.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {
    @GetMapping("/contact")
    public String introduce(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "contact";
    }
}

package com.example.j2ee_frontend_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CharityController {

    @GetMapping("/charities")
    public String showProjectsPage() {
        return "charity";
    }
}


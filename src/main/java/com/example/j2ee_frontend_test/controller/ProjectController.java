package com.example.j2ee_frontend_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectController {

    @GetMapping("/projects")
    public String showProjectsPage() {
        return "project";
    }
}


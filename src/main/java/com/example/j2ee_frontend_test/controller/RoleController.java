package com.example.j2ee_frontend_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @GetMapping
    public String getAllRoles() {
        return "role"; // Trả về tên template Thymeleaf
    }
}

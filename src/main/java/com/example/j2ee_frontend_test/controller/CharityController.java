package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.CharityEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.event.ChangeEvent;

@Controller
@RequestMapping("/charities")
public class CharityController {

    @GetMapping
    public String showProjectsPage() {
        return "charity";
    }
    @GetMapping("/new")
    public String showNewCharityPage(Model model) {
        CharityEvent c= new CharityEvent();
        model.addAttribute("charity",c);
        return "create_charity";
    }
}


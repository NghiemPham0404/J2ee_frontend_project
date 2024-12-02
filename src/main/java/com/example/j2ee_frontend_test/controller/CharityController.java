package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.response.CharityListResponse;
import com.example.j2ee_frontend_test.services.CharityService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.event.ChangeEvent;

@Controller
@RequestMapping("/charities")
public class CharityController {

    @Autowired
    CharityService charityService;

    @GetMapping
    public String showProjectsPage(Model model,@PathParam("page") Integer page) {
        page = page != null ? page - 1: 0;
        CharityListResponse charityListResponse=charityService.getAllCharities(1, page);
        model.addAttribute("data", charityListResponse.getCharityList());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", charityListResponse.getTotalPages());
        model.addAttribute("total_results", charityListResponse.getTotalResults());
        return "charity";
    }
    @GetMapping("/new")
    public String showNewCharityPage(Model model) {
        CharityEvent c= new CharityEvent();
        model.addAttribute("charity",c);
        return "create_charity";
    }
    @GetMapping("/detail")
    public String showDetailCharityPage(Model model) {
        CharityEvent c= new CharityEvent();
        model.addAttribute("charity",c);
        return "detail_charity";
    }
}


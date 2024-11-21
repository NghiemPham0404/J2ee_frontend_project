package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.response.CharityListResponse;
import com.example.j2ee_frontend_test.services.CharityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.event.ChangeEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/charity-events")
public class CharityController {

    @Autowired
    CharityService charityService;
    @GetMapping
    public String showProjectsPage(Model model,@PathParam("page") Integer page) {
        page = page != null ? page - 1: 0;
        CharityListResponse charityListResponse=charityService.getAllCharities(1,page);
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
    @GetMapping("/{id}")
    public String showDetailCharityPage(Model model,@PathVariable("id") UUID id) {
        CharityEvent c=charityService.getCharityById(id);

        model.addAttribute("data",c);

        return "detail_charity";
    }
    @PostMapping("/save")
    public String saveCharity(@ModelAttribute("charity") CharityEvent charityEvent) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String charityJson = objectMapper.writeValueAsString(charityEvent);
        System.out.println(charityJson);
        if (charityEvent.getId() != null) {
            charityService.updateCharity(charityEvent.getId(),charityEvent);
        } else {
            charityEvent.generateUUID();
            charityEvent.setDisbursed(false);
            Date s=new Date(2024, 10, 20, 20, 47, 30);
            Date e=new Date(2024, 11, 20, 20, 47, 30);
            charityEvent.setStartTime(s);
            charityEvent.setEndTime(e);
            charityService.createCharity(charityEvent);
        }
        return "redirect:/charity-events";
    }
    @GetMapping("/delete/{id}")
    public String deleteCharity(@PathVariable("id") UUID id,Model model){
        String c=charityService.deleteCharity(id);
        model.addAttribute("msg",c);
        return "redirect:/charity-events";

    }
}


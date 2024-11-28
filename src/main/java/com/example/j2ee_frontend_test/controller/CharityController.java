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
import java.util.TimeZone;
import java.util.UUID;

@Controller
@RequestMapping("/charity-events")
public class CharityController {

    @Autowired
    CharityService charityService;

    @GetMapping
    public String showProjectsPage(Model model,@PathParam("query") String query,@PathParam("page") Integer page) {
        page = page != null ? page : 0;
        CharityListResponse charityListResponse;
        if(query==null) {
             charityListResponse = charityService.getAllCharities(1, page);
        } else
        { charityListResponse = charityService.searchNameCharities( query,page); }
        System.out.println(charityListResponse.getCharityList());
        model.addAttribute("data", charityListResponse.getCharityList());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", charityListResponse.getTotalPages());
        model.addAttribute("total_results", charityListResponse.getTotalResults());
        model.addAttribute("query",query);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartTime = sdf.format(c.getStartTime());
        String formattedEndTime = sdf.format(c.getEndTime());
        model.addAttribute("data",c);
        model.addAttribute("startTime", formattedStartTime);
        model.addAttribute("endTime", formattedEndTime);
        return "detail_charity";
    }
    @PostMapping("/save")
    public String saveCharity(@ModelAttribute("charity") CharityEvent charityEvent) throws Exception{
        if (charityEvent.getId() != null) {
            charityService.updateCharity(charityEvent.getId(),charityEvent);
        } else {
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


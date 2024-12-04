package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.response.CharityListResponse;
import com.example.j2ee_frontend_test.services.CharityService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/disburse")
public class DisburseController {
    @Autowired
    CharityService charityService;
    @GetMapping
    public String showDisburse(Model model, @PathParam("query") String query, @PathParam("page") Integer page){
        page = page != null ? page : 0 ;
        CharityListResponse charityListResponse;
        charityListResponse=charityService.notDisburseCharity(page);
        int total =charityListResponse.getTotalPages();
        model.addAttribute("data",charityListResponse.getCharityList());
        model.addAttribute("page",page);
        model.addAttribute("total_pages",total);
        return "disburse";
    }
    @PostMapping("/{id}")
    public String disburse(@PathVariable("id") UUID id, Model model) {
        String c=charityService.disburseCharity(id);
        System.out.println(c);
        boolean success = disburseFunds(id);
        model.addAttribute("message", success ? "Giải ngân thành công!" : "Giải ngân thất bại!");
        model.addAttribute("id", id);

        return "disburse-confirm";
    }

    private boolean disburseFunds(UUID id) {
        System.out.println("Đang giải ngân cho chương trình ID: " + id);
        return true;
    }
}

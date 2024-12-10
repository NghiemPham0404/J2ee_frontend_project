package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.response.CharityListResponse;
import com.example.j2ee_frontend_test.services.CharityService;
import com.example.j2ee_frontend_test.services.StatisticService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/disburse")
public class DisburseController {
    @Autowired
    CharityService charityService;
    @Autowired
    StatisticService statisticService;
    @GetMapping
    public String showDisburse(Model model, @PathParam("query") String query, @PathParam("page") Integer page,@PathParam("startDate") String startDate,
                               @PathParam("endDate") String endDate) {
        page = page != null ? page : 0;
        CharityListResponse charityListResponse;
        charityListResponse = charityService.notDisburseCharity(page);
        if (charityListResponse != null) {
            int total = charityListResponse.getTotalPages();
            model.addAttribute("data", charityListResponse.getCharityList());
            model.addAttribute("page", page);
            model.addAttribute("total_pages", total);
        }else{
            int total = 0;
            model.addAttribute("data", new ArrayList<CharityEvent>());
            model.addAttribute("page", page);
            model.addAttribute("total_pages", total);
        }
        LocalDate today = LocalDate.now();
        if (startDate !=null && endDate != null){
            String[] startParts = startDate.split("/");
            String[] endParts = endDate.split("/");
            int startYear = Integer.parseInt(startParts[0]);
            int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
            int startDay = Integer.parseInt(startParts[2]);

            int endYear = Integer.parseInt(endParts[0]);
            int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
            int endDay = Integer.parseInt(endParts[2]);

            // Tạo đối tượng Date từ các giá trị đã tách
            Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
            Date end = new Date(endYear - 1900, endMonth, endDay);
            CharityListResponse cListResponse = statisticService.disburseCharity(start, end);
            List<CharityEvent> data = cListResponse.getCharityList();
            model.addAttribute("done",data);
            model.addAttribute("startDate",start);
            model.addAttribute("endDate",end);
            model.addAttribute("start",startDate);
            model.addAttribute("end",endDate);
        } else {
            startDate = today.minusDays(30).toString();
            endDate=today.toString();
            String[] startParts = startDate.split("-");
            String[] endParts = endDate.split("-");
            int startYear = Integer.parseInt(startParts[0]);
            int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
            int startDay = Integer.parseInt(startParts[2]);

            int endYear = Integer.parseInt(endParts[0]);
            int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
            int endDay = Integer.parseInt(endParts[2]);
            Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
            Date end = new Date(endYear - 1900, endMonth, endDay);
            CharityListResponse cListResponse = statisticService.disburseCharity(start, end);
            List<CharityEvent> data = cListResponse.getCharityList();
            model.addAttribute("done",data);
            model.addAttribute("startDate",start);
            model.addAttribute("endDate",end);
            model.addAttribute("start",startDate);
            model.addAttribute("end",endDate);
        }
        return "disburse";
    }

    @PostMapping("/{id}")
    public String disburse(@PathVariable("id") String id, Model model) {
        charityService.disburseCharity(id);
        String name = charityService.getCharityById(UUID.fromString(id)).getName();
        model.addAttribute("message", true ? "Giải ngân thành công!" : "Giải ngân thất bại!");
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        return "disburse-confirm";
    }
}

package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.response.CharityListResponse;
import com.example.j2ee_frontend_test.response.MostPostAccountListResponse;
import com.example.j2ee_frontend_test.services.StatisticService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @GetMapping
    public String showStatisticPage(Model model) {
        try {
            // Định nghĩa định dạng ngày
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");


            Date startDate = dateFormat.parse("2023/01/01");
            Date endDate = dateFormat.parse("2025/01/01");
            System.out.println(startDate);
            System.out.println(endDate);
            // Gọi service với giá trị Date
            MostPostAccountListResponse mostPostAccountListResponse = statisticService.getMostPostAccount(dateFormat.parse("2023/01/01"),dateFormat.parse("2025/01/01"));

            // Thêm vào model giá trị đã định dạng
            model.addAttribute("accounts", mostPostAccountListResponse);
            model.addAttribute("startDate", dateFormat.format(startDate)); // Định dạng lại thành chuỗi
            model.addAttribute("endDate", dateFormat.format(endDate));     // Định dạng lại thành chuỗi

        } catch (ParseException e) {
            e.printStackTrace();
            return "error"; // Trang lỗi nếu có vấn đề với định dạng
        }

        return "statistic";
    }
}

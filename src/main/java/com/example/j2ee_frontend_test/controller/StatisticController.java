package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.DTOs.MostCharitablePeopleDTO;
import com.example.j2ee_frontend_test.DTOs.MostDonationEventsDTO;
import com.example.j2ee_frontend_test.DTOs.MostPostsAccountsDTO;
import com.example.j2ee_frontend_test.DTOs.MostViewedPostsDTO;
import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.response.*;
import com.example.j2ee_frontend_test.services.CharityService;
import com.example.j2ee_frontend_test.services.StatisticService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    StatisticService statisticService;
    @Autowired
    CharityService charityService;
    @GetMapping("")
    public String showStatisticPage(Model model,
                                    @PathParam("ce_id") UUID ce_id,
                                    @PathParam("startDate") String startDate,
                                    @PathParam("endDate") String endDate) {
        ce_id = ce_id !=null? ce_id : UUID.fromString("37313236-6663-3238-2d64-3633322d3461");
        String name=charityService.getCharityById(ce_id).getName();
        startDate= startDate != null ? startDate : "2023/01/01";
        endDate=endDate != null? endDate: "2025/01/01";
        // Tách chuỗi ngày tháng theo dấu "/"
        String[] startParts = startDate.split("/");
        String[] endParts = endDate.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);

        // Tạo đối tượng Date từ các giá trị đã tách
        Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date end = new Date(endYear - 1900, endMonth, endDay); // Năm cần giảm đi 1900
        // Kiểm tra giá trị Date đã được tạo thành công
        System.out.println("Start Date: " + start);
        System.out.println("End Date: " + end);
        MostPostAccountListResponse mostPostAccountListResponse = statisticService.getMostPostAccount(start, end);
        List<MostPostsAccountsDTO> chart4 = mostPostAccountListResponse.getMostPostAccountList();
        MostDonationEventListResponse mostDonationEventListResponse= statisticService.getMostDonationEvent(start,end);
        List<MostDonationEventsDTO> chart3=mostDonationEventListResponse.getMostDonationEventList();
        MostViewedPostListResponse mostViewedPostListResponse= statisticService.getMostViewedPosts(start,end);
        List<MostViewedPostsDTO> chart1=mostViewedPostListResponse.getMostViewedPostList();
        MostCharitableListResponse mostCharitableListResponse=statisticService.getMostCharitable(ce_id,start,end);
        List<MostCharitablePeopleDTO> chart2=mostCharitableListResponse.getMostCharitableList();

        List<CharityEvent> allCharities = new ArrayList<>();

        int totalPages = charityService.getAllCharities(0).getTotalPages();
                for (int page = 0; page < totalPages; page++) {
            CharityListResponse response = charityService.getAllCharities( page);
            allCharities.addAll(response.getCharityList());
        }
        model.addAttribute("chart4", chart4);
        model.addAttribute("chart3",chart3);
        model.addAttribute("chart1",chart1);
        model.addAttribute("chart2",chart2);
        model.addAttribute("list",allCharities);
        model.addAttribute("startDate",start);
        model.addAttribute("endDate",end);
        model.addAttribute("id",ce_id);

        return "statistic";
    }
}

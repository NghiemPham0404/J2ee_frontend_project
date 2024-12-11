package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.DTOs.BalanceTrackingDTO;
import com.example.j2ee_frontend_test.services.BalanceTrackingService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/balance-tracking")
public class BalanceTrackingController {

    @Autowired
    private BalanceTrackingService balanceTrackingService;

    @GetMapping
    public String balanceTracking(@PathParam("year") Integer year, Model model) {
        BigDecimal remainBalance = balanceTrackingService.getRemainBalance();
        List<Integer> allActiveYear = balanceTrackingService.getAllActiveYear();

        year = (year != null) ? year : (!allActiveYear.isEmpty() ? allActiveYear.get(0) : (new Date(System.currentTimeMillis())).getYear());
        List<BalanceTrackingDTO> data = balanceTrackingService.balanceTracking(year);

        List<Integer> months = new ArrayList<>();
        List<BigDecimal> transferTotals = new ArrayList<>();
        List<BigDecimal> charityEventTotals = new ArrayList<>();

        for (BalanceTrackingDTO dto : data) {
            months.add(dto.getMonth());
            transferTotals.add(dto.getTransferTotal());
            charityEventTotals.add(dto.getCharityEventDisburse());
            System.out.printf("%d %s %s \n", dto.getMonth(), dto.getTransferTotal(), dto.getCharityEventDisburse());
        }
        BigDecimal transferTotalsSum = transferTotals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal disburseTotalsSum = charityEventTotals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("remainBalance", remainBalance);
        model.addAttribute("allActiveYear", allActiveYear);
        model.addAttribute("data", data);
        model.addAttribute("months", months);
        model.addAttribute("transferTotals", transferTotals);
        model.addAttribute("disburseTotals", charityEventTotals);
        model.addAttribute("transferTotalsSum", transferTotalsSum);
        model.addAttribute("disburseTotalsSum", disburseTotalsSum);

        return "statistic/balance-tracking";
    }
}

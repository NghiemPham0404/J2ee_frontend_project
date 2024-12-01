package com.example.j2ee_frontend_test.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostDonationEventsDTO {
    private UUID charityEventId;
    private String name;
    private BigDecimal totalDonationAmount;
    private BigDecimal goalAmount;
}

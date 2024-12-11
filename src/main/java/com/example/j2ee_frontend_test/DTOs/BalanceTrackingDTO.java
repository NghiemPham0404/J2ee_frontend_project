package com.example.j2ee_frontend_test.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceTrackingDTO {
     int month;
     BigDecimal transferTotal;
     BigDecimal charityEventDisburse;
}

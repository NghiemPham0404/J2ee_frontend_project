package com.example.j2ee_frontend_test.models;

//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private Long id;
    private String orderId;
    private String transactionId;
    private String totalPrice;
    private LocalDateTime paymentTime;
    private int paymentStatus;
    private int display;
}

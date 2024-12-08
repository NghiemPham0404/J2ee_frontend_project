package com.example.j2ee_frontend_test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharityEvent {

    private UUID id;

    private String name;

    private String description;

    private Date startTime;

    private Date endTime;

    private BigDecimal goalAmount;

    private Date isDisbursed;

    public void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    List<Post> posts;

    List<TransferSession> transferSessions;

//    public BigDecimal getCurrentAmount() {
//        BigDecimal currentAmount = BigDecimal.ZERO;
//        if (transferSessions != null) {
//            for (TransferSession session : transferSessions) {
//                if (session != null && session.getAmount() != null) {
//                    currentAmount = currentAmount.add(session.getAmount());
//                }
//            }
//        }
//        return currentAmount;
//    }

    private BigDecimal currentAmount;

    private String timeLeft;
}


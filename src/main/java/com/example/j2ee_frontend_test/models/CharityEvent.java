package com.example.j2ee_frontend_test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
public class CharityEvent {

    private UUID id;

    private String name;

    private String description;

    private Date startTime;

    private Date endTime;

    private BigDecimal goalAmount;

    private boolean isDisbursed;

    public void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    List<Post> posts;

    List<TransferSession> transferSessions;

    public BigDecimal getCurrentAmount() {
        BigDecimal currentAmount = new BigDecimal(0);
        for(TransferSession session : transferSessions) {
            currentAmount = currentAmount.add(session.getAmount());
        }
        return currentAmount;
    }
}


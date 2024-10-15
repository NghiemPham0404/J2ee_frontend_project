package com.example.j2ee_frontend_test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferSession {

    private UUID id;

    private String name;

    private BigDecimal amount;

    private String description;

    private LocalDateTime time;

    private CharityEvent charityEvent;

    public void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}

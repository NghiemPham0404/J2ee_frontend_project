package com.example.j2ee_frontend_test.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificationDTO {
    private String recipientAddress;
    private String subject;
    private String body;
    private CertificationInfo info;

    public String getTransferSessionId(){
        return info.getTransferSessionId();
    }
    public String getFullName(){
        return info.getFullName();
    }
    public String getEvent(){
        return info.getEvent();
    }
    public String getTime(){
        return info.getTime();
    }
    public String getDonation(){
        return info.getDonation();
    }

    @Data
    class CertificationInfo {
        private String transferSessionId;
        private String fullName;
        private String event;
        private String time;
        private String donation;
    }
}
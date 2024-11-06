package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.TransferSession;
import com.example.j2ee_frontend_test.response.TransferSessionListResponse;
import com.example.j2ee_frontend_test.services.TransferSessionService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransferSessionController {
    @Autowired
    TransferSessionService transferSessionService;

    @GetMapping("/{eventId}")
    public String viewTransactionPage(@PathVariable("eventId") String eventId, Model model, @PathParam("page") Integer page) {
        page = page != null ? page - 1: 0;
        TransferSessionListResponse transferSessionListResponse = transferSessionService.getTransferSessionsByEvent(eventId, page);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<TransferSession> transferSessions = transferSessionListResponse.getTransferSessionList();
        for (TransferSession transferSession : transferSessions) {
            transferSession.setFormattedTime(transferSession.getTime().format(formatter));
        }
        model.addAttribute("data", transferSessions);
        model.addAttribute("page", page);
        model.addAttribute("total_pages", transferSessionListResponse.getTotalPages());
        model.addAttribute("total_results", transferSessionListResponse.getTotalResults());

        System.out.println(transferSessionListResponse.getTransferSessionList().size());

        return "transaction_statements";
    }


}

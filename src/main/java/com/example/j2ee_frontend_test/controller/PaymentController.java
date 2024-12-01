package com.example.j2ee_frontend_test.controller;


import com.example.j2ee_frontend_test.services.VNPayService;
import com.example.j2ee_frontend_test.models.Artical;
//import com.example.j2ee_frontend_test.Model.FundraisingCampaign_model;
import com.example.j2ee_frontend_test.models.Payment;
import com.example.j2ee_frontend_test.services.apis.CharityApi;
//import com.example.j2ee_frontend_test.Repository.FundraisingCampaign_Repo;
import com.example.j2ee_frontend_test.services.apis.PaymentApi;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@org.springframework.stereotype.Controller
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private Payment payment;

    @Autowired
    private CharityApi articalRepo;

    @GetMapping("/thanh-toan")
    public String home(){
        return "payment/payment_user";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        // Định dạng lại paymentTime
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime parsedPaymentTime = LocalDateTime.parse(paymentTime, inputFormatter);
            String formattedPaymentTime = parsedPaymentTime.format(outputFormatter);

            // Cắt bỏ 2 số cuối của vnp_Amount
            String formattedTotalPrice = totalPrice.substring(0, totalPrice.length() - 2);
            double totalAmount = Double.parseDouble(formattedTotalPrice);

            // Lưu thông tin thanh toán vào cơ sở dữ liệu
            Payment payment = new Payment();
            payment.setOrderId(orderInfo);
            payment.setTotalPrice(String.valueOf(totalAmount));
            payment.setTransactionId(transactionId);
            payment.setPaymentTime(parsedPaymentTime);
            payment.setPaymentStatus(paymentStatus);
            payment.setDisplay(0);
            PaymentApi.save(payment);

            // Nếu thanh toán thành công, cập nhật số tiền đã gây quỹ
            if (paymentStatus == 1 && orderInfo != null && !orderInfo.trim().isEmpty()) {
                String lastFiveChars = orderInfo.replaceAll("\\s", "").substring(orderInfo.length() - 5);

                // Cập nhật FundraisingCampaign_model
                FundraisingCampaign_model campaign = fundraisingCampaignRepo.findByCode(lastFiveChars).stream()
                        .findFirst()
                        .orElse(null);

                if (campaign != null) {
                    if ("1".equals(campaign.getStatus())) {
                        updateDefaultArticle(totalPrice);
                        model.addAttribute("message", "Hoàn cảnh bạn muốn quyên góp đã bị đóng không thể quyên góp được nữa và tiền bạn đóng góp đã được chuyển vào quỹ chung của tổ chức. Nếu bạn có thắc mắc và muốn giúp đỡ vui lòng liên hệ đến SĐT: 0898502822");
                        model.addAttribute("orderId", orderInfo);
                        model.addAttribute("totalPrice", totalPrice);
                        model.addAttribute("paymentTime", formattedPaymentTime);
                        model.addAttribute("transactionId", transactionId);
                        return "payment/ordersuccess2";
                    }

                    if ("0".equals(campaign.getStatus())) {
                        Long campaignId = campaign.getId();
                        campaign = fundraisingCampaignRepo.findById(campaignId).orElse(null);
                        if (campaign != null) {
                            double amountRaised = campaign.getAmountRaised() + Double.parseDouble(totalPrice) / 100; // Chia 100 vì vnp_Amount nhân 100
                            campaign.setAmountRaised(amountRaised);
                            fundraisingCampaignRepo.save(campaign);
                        }
                    }
                }

                // Cập nhật Artical_model
                Artical artical = articalRepo.findByCode(lastFiveChars).stream()
                        .findFirst()
                        .orElse(null);

                // Nếu không tìm thấy, tìm bài viết mặc định QC000
                if (artical == null || "1".equals(artical.getStatus())) {
                    updateDefaultArticle(totalPrice);
                    model.addAttribute("message", "Hoàn cảnh bạn muốn quyên góp đã bị đóng không thể quyên góp được nữa và tiền bạn đóng góp đã được chuyển vào quỹ chung của tổ chức. Nếu bạn có thắc mắc và muốn giúp đỡ vui lòng liên hệ đến SĐT: 0898502822");
                    model.addAttribute("orderId", orderInfo);
                    model.addAttribute("totalPrice", totalPrice);
                    model.addAttribute("paymentTime", formattedPaymentTime);
                    model.addAttribute("transactionId", transactionId);
                    return "payment/ordersuccess2";
                } else {
                    // Cộng dồn số tiền vào bài viết tìm được
                    double amountRaisedArtical = artical.getAmountRaised() + Double.parseDouble(totalPrice) / 100; // Chia 100 vì vnp_Amount nhân 100
                    artical.setAmountRaised(amountRaisedArtical);
                    articalRepo.save(artical);
                }
            }

            // Truyền thông tin đã định dạng vào model để hiển thị
            model.addAttribute("orderId", orderInfo);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("paymentTime", formattedPaymentTime);
            model.addAttribute("transactionId", transactionId);

            return paymentStatus == 1 ? "payment/ordersuccess" : "payment/orderfail";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Đã xảy ra lỗi khi xử lý thanh toán: " + e.getMessage());
            return "payment/orderfail";
        }
    }

    private void updateDefaultArticle(String totalPrice) {
        Artical defaultArtical = articalRepo.findByCode("QC000").stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(""));
        double amountRaisedArtical = defaultArtical.getAmountRaised() + Double.parseDouble(totalPrice) / 100; // Chia 100 vì vnp_Amount nhân 100
        defaultArtical.setAmountRaised(amountRaisedArtical);
        articalRepo.save(defaultArtical);
    }

}

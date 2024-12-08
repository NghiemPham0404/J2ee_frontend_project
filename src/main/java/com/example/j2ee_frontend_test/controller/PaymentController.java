package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.DTOs.CertificateContext;
import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.models.Email;
import com.example.j2ee_frontend_test.models.TransferSession;
import com.example.j2ee_frontend_test.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@SessionAttributes({"fullName", "orderInfo"})
@org.springframework.stereotype.Controller
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private TransferSessionService transferSessionService;

    @Autowired
    private PostService postService;

    @Autowired
    private EmailService emailService; // Inject EmailService


    @GetMapping("/donate/{post_id}")
    public String home(@PathVariable String post_id, Model model) {
        CharityEvent charityEvent = postService.getPostByIdForUser(UUID.fromString(post_id)).getCharityEvent();
        model.addAttribute("event_name", charityEvent.getName());
        model.addAttribute("post_id", post_id);
        return "payment/payment_user";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("fullname") String fullname,
                              @RequestParam("post_id") String postId,
                              HttpServletRequest request,
                              Model model) {
        model.addAttribute("fullName", fullname);
        model.addAttribute("orderInfo", orderInfo);
        orderInfo = fullname + "_" + postId + "_" + orderInfo;
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
            Date time = Date.from(parsedPaymentTime.atZone(ZoneId.systemDefault()).toInstant());
            String formattedPaymentTime = parsedPaymentTime.format(outputFormatter);

            // Cắt bỏ 2 số cuối của vnp_Amount
            String formattedTotalPrice = totalPrice.substring(0, totalPrice.length() - 2);
            BigDecimal totalAmount = BigDecimal.valueOf(Double.parseDouble(formattedTotalPrice));

            if (orderInfo != null && !orderInfo.trim().isEmpty()) {
                try {
                    // Tách thông tin từ orderInfo
                    String[] orderInfoSplit = orderInfo.trim().split("_");
                    if (orderInfoSplit.length < 3) {
                        // Nếu không đủ thông tin, trả về lỗi
                        model.addAttribute("error", "Thông tin đơn hàng không đầy đủ.");
                        return "payment/orderfail";
                    }

                    String fullname = model.getAttribute("fullName").toString();
                    String postId = orderInfoSplit[1];
                    String eventName = postService.getPostByIdForUser(UUID.fromString(postId)).getCharityEvent().getName();
                    String description = model.getAttribute("orderInfo").toString();

                    // Nếu trạng thái thanh toán là 1, xử lý thêm thông tin giao dịch
                    if (paymentStatus == 1) {
                        // Tạo đối tượng TransferSession
                        TransferSession transferSession = new TransferSession();
                        transferSession.setName(fullname);
                        transferSession.setDescription(description);
                        transferSession.setAmount(totalAmount);
                        transferSession.setTime(time);

                        // Lưu thông tin chuyển khoản
                        String transferId = transferSessionService.recordTransferSession(postService.getPostByIdForUser(UUID.fromString(postId)).getCharityEvent().getId().toString(), transferSession).getMessage();
                        model.addAttribute("transferId", transferId);
                    }

                    // Truyền thông tin vào model (áp dụng cho cả ordersuccess và orderfail)
                    model.addAttribute("fullname", fullname);
                    model.addAttribute("event", eventName);
                    model.addAttribute("orderId", orderInfo);
                    model.addAttribute("totalPrice", totalPrice);
                    model.addAttribute("paymentTime", formattedPaymentTime);
                    model.addAttribute("transactionId", transactionId);

                    // Trả về trang phù hợp
                    return paymentStatus == 1 ? "payment/ordersuccess" : "payment/orderfail";
                } catch (Exception e) {
                    // Xử lý lỗi (nếu có bất kỳ ngoại lệ nào)
                    e.printStackTrace();
                    model.addAttribute("error", "Đã xảy ra lỗi trong quá trình xử lý.");
                    return "payment/orderfail";
                }
            } else {
                // Trường hợp thông tin orderInfo không hợp lệ
                model.addAttribute("error", "Thông tin đơn hàng không hợp lệ.");
                return "payment/orderfail";
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Đã xảy ra lỗi khi xử lý thanh toán: " + e.getMessage());
            return "payment/orderfail";
        }
    }

    @PostMapping("/certification")
    public String certification(@RequestParam("transferId") String transferId,
                                @RequestParam("emailAddress") String emailAddress) {

        // Gửi email cho người dùng
        emailService.sendCertification(emailAddress, transferId);
        return "redirect:/";
    }
}

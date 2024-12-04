package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.models.TransferSession;
import com.example.j2ee_frontend_test.services.CharityService;
import com.example.j2ee_frontend_test.services.TransferSessionService;
import com.example.j2ee_frontend_test.services.VNPayService;
import com.example.j2ee_frontend_test.services.EmailService; // Import EmailService
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@org.springframework.stereotype.Controller
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private TransferSessionService transferSessionService;

    @Autowired
    private CharityService charityService;

    @Autowired
    private EmailService emailService; // Inject EmailService

    @GetMapping("/donate/{event_id}")
    public String home(@PathVariable String event_id, Model model) {
        CharityEvent charityEvent = charityService.getCharityById(UUID.fromString(event_id));
        model.addAttribute("event_name", charityEvent.getName());
        model.addAttribute("event_id", event_id);
        return "payment/payment_user";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("fullname") String fullname,
                              @RequestParam("eventId") String eventId,
                              HttpServletRequest request) {
        orderInfo = fullname + "_" + eventId + "_" + orderInfo;
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

                    String fullname = orderInfoSplit[0];
                    String eventId = orderInfoSplit[1];
                    String description = orderInfoSplit[2];

                    // Nếu trạng thái thanh toán là 1, xử lý thêm thông tin giao dịch
                    if (paymentStatus == 1) {
                        // Tạo đối tượng TransferSession
                        TransferSession transferSession = new TransferSession();
                        transferSession.setName(fullname);
                        transferSession.setDescription(description);
                        transferSession.setCharityEvent(charityService.getCharityById(UUID.fromString(eventId)));
                        transferSession.setAmount(totalAmount);
                        transferSession.setTime(time);

                        // Lưu thông tin chuyển khoản
                        transferSessionService.recordTransferSession(eventId, transferSession);

                        // Gửi email cho người dùng
                        String emailSubject = "Xác nhận thanh toán thành công";
                        String emailBody = "Cảm ơn bạn đã tham gia đóng góp!\n\n" +
                                "Chi tiết giao dịch:\n" +
                                "Mã đơn hàng: " + orderInfo + "\n" +
                                "Tổng tiền: " + totalPrice + "\n" +
                                "Thời gian thanh toán: " + formattedPaymentTime + "\n" +
                                "Mã giao dịch: " + transactionId + "\n\n" +
                                "Chúc bạn một ngày tốt lành!";
                        String userEmail = request.getParameter("userEmail"); // Lấy email từ form (có thể thay đổi theo yêu cầu)
                        emailService.sendEmail(Email,Subject, Body);
                    }

                    // Truyền thông tin vào model (áp dụng cho cả ordersuccess và orderfail)
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
}

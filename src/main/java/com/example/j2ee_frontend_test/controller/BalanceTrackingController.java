package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.DTOs.BalanceTrackingDTO;
import com.example.j2ee_frontend_test.services.BalanceTrackingService;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import retrofit2.http.Query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/balance-tracking")
public class BalanceTrackingController {

    @Autowired
    private BalanceTrackingService balanceTrackingService;
    @Value("${font.path}")
    private String fontPath;
    @GetMapping
    public String balanceTracking(@PathParam("year") Integer year, Model model) {
        BigDecimal remainBalance = balanceTrackingService.getRemainBalance();
        List<Integer> allActiveYear = balanceTrackingService.getAllActiveYear();

        year = (year != null) ? year : (!allActiveYear.isEmpty() ? allActiveYear.get(0) : (new Date(System.currentTimeMillis())).getYear());
        List<BalanceTrackingDTO> data = balanceTrackingService.balanceTracking(year);

        List<Integer> months = new ArrayList<>();
        List<BigDecimal> transferTotals = new ArrayList<>();
        List<BigDecimal> charityEventTotals = new ArrayList<>();

        for (BalanceTrackingDTO dto : data) {
            months.add(dto.getMonth());
            transferTotals.add(dto.getTransferTotal());
            charityEventTotals.add(dto.getCharityEventDisburse());
            System.out.printf("%d %s %s \n", dto.getMonth(), dto.getTransferTotal(), dto.getCharityEventDisburse());
        }
        BigDecimal transferTotalsSum = transferTotals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal disburseTotalsSum = charityEventTotals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("remainBalance", remainBalance);
        model.addAttribute("allActiveYear", allActiveYear);
        model.addAttribute("data", data);
        model.addAttribute("months", months);
        model.addAttribute("transferTotals", transferTotals);
        model.addAttribute("disburseTotals", charityEventTotals);
        model.addAttribute("transferTotalsSum", transferTotalsSum);
        model.addAttribute("disburseTotalsSum", disburseTotalsSum);

        return "statistic/balance-tracking";
    }
    @GetMapping("/export")
    public ResponseEntity<byte[]> balancePDF(@Query("year") Integer year, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

        // Tạo PdfWriter và PdfDocument
        PdfWriter writer = new PdfWriter(pdfStream);
        PdfDocument pdfDocument = new PdfDocument(writer);

        // Chỉ định font Times New Roman
        PdfFont font = PdfFontFactory.createFont(fontPath, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        // Tạo một tài liệu PDF với font Times New Roman
        Document document = new Document(pdfDocument);
        document.setFont(font); // Thiết lập font cho toàn bộ tài liệu

        // Nội dung HTML cần chuyển đổi thành PDF
        String htmlContent = BalancePDF(year);

        // Sử dụng HtmlConverter để chuyển HTML thành PDF với font Times New Roman
        HtmlConverter.convertToPdf(htmlContent, pdfDocument.getWriter());




        // Cấu hình header để trả về file PDF cho người dùng
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+year+"_report.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");



        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
    private String BalancePDF(Integer year) {
        List<BalanceTrackingDTO> data = balanceTrackingService.balanceTracking(year);
//        List<Integer> months = new ArrayList<>();
//        List<BigDecimal> transferTotals = new ArrayList<>();
//        List<BigDecimal> charityEventTotals = new ArrayList<>();
//        for (BalanceTrackingDTO dto : data) {
//            months.add(dto.getMonth());
//            transferTotals.add(dto.getTransferTotal());
//            charityEventTotals.add(dto.getCharityEventDisburse());
//            System.out.printf("%d %s %s \n", dto.getMonth(), dto.getTransferTotal(), dto.getCharityEventDisburse());
//        }

        // Tạo nội dung HTML
        StringBuilder htmlContent = new StringBuilder("<html><head><style>body { font-family: 'Times New Roman'; }</style></head><body>");
        htmlContent.append("<h1>Thống kê tài chính năm ").append(year).append("</h1>");
        htmlContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        htmlContent.append("<thead><tr><th>Tháng</th><th>Số tiền chuyển khoản nhận được</th><th>Số tiền giải ngân</th></tr></thead>");
        htmlContent.append("<tbody>");

        // Duyệt qua danh sách sao kê và tạo các dòng trong bảng HTML

        for (BalanceTrackingDTO list : data) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(list.getMonth()).append("</td>");
            htmlContent.append("<td>").append(list.getTransferTotal()).append("</td>");
            htmlContent.append("<td>").append(list.getCharityEventDisburse()).append("</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }
}

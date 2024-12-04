package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.TransferSession;
import com.example.j2ee_frontend_test.response.TransferSessionListResponse;
import com.example.j2ee_frontend_test.services.TransferSessionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.servlet.http.HttpServletResponse;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransferSessionController {
    @Autowired
    TransferSessionService transferSessionService;

    @Value("${font.path}")
    private String fontPath;

    @GetMapping("/{eventId}")
    public String viewTransactionPage(@PathVariable("eventId") String eventId, Model model) {

        TransferSessionListResponse transferSessionListResponse = transferSessionService.getTransferSessionsByEvent(eventId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<TransferSession> transferSessions = transferSessionListResponse.getTransferSessionList();
        for (TransferSession transferSession : transferSessions) {
            transferSession.setFormattedTime(transferSession.getTime().toString());
        }
        model.addAttribute("data", transferSessions);
        System.out.println(transferSessions);
        model.addAttribute("total_pages", transferSessionListResponse.getTotalPages());
        model.addAttribute("total_results", transferSessionListResponse.getTotalResults());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String json = objectMapper.writeValueAsString(transferSessions);
            model.addAttribute("json", json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "transaction_statements";
    }

    @GetMapping("/export/{eventId}")
    public ResponseEntity<byte[]> exportToPDF(@PathVariable String eventId, Model model, HttpServletResponse response) throws IOException {
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
        String htmlContent = generateHtmlContentForPDF(eventId);

        // Sử dụng HtmlConverter để chuyển HTML thành PDF với font Times New Roman
        HtmlConverter.convertToPdf(htmlContent, pdfDocument.getWriter());




        // Cấu hình header để trả về file PDF cho người dùng
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sao_ke.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");



        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }

    private String generateHtmlContentForPDF(String eventId) {
        TransferSessionListResponse transferSessionListResponse = transferSessionService.getTransferSessionsByEvent(eventId);
        List<TransferSession> transferSessions = transferSessionListResponse.getTransferSessionList();


        // Tạo nội dung HTML
        StringBuilder htmlContent = new StringBuilder("<html><head><style>body { font-family: 'Times New Roman'; }</style></head><body>");
        htmlContent.append("<h1>Danh sách sao kê</h1>");
        htmlContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        htmlContent.append("<thead><tr><th>ID</th><th>Tên giao dịch</th><th>Số tiền quyên góp</th><th>Nội dung giao dịch</th><th>Thời gian</th></tr></thead>");
        htmlContent.append("<tbody>");

        // Duyệt qua danh sách sao kê và tạo các dòng trong bảng HTML
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for (TransferSession transferSession : transferSessions) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(transferSession.getId()).append("</td>");
            htmlContent.append("<td>").append(transferSession.getName()).append("</td>");
            htmlContent.append("<td>").append(transferSession.getAmount()).append(" VND</td>");
            htmlContent.append("<td>").append(transferSession.getDescription()).append("</td>");
            htmlContent.append("<td>").append(transferSession.getTime().format(formatter)).append("</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }

}

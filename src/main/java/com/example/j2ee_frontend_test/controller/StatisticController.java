package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.DTOs.MostCharitablePeopleDTO;
import com.example.j2ee_frontend_test.DTOs.MostDonationEventsDTO;
import com.example.j2ee_frontend_test.DTOs.MostPostsAccountsDTO;
import com.example.j2ee_frontend_test.DTOs.MostViewedPostsDTO;
import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.models.TransferSession;
import com.example.j2ee_frontend_test.response.*;
import com.example.j2ee_frontend_test.services.CharityService;
import com.example.j2ee_frontend_test.services.StatisticService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.Query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    StatisticService statisticService;
    @Autowired
    CharityService charityService;
    @Value("${font.path}")
    private String fontPath;
    @GetMapping("")
    public String showStatisticPage(Model model,
                                    @PathParam("ce_id") UUID ce_id,
                                    @PathParam("startDate") String startDate,
                                    @PathParam("endDate") String endDate) {
        ce_id = ce_id !=null? ce_id : UUID.fromString("37313236-6663-3238-2d64-3633322d3461");
        String name=charityService.getCharityById(ce_id).getName();
        startDate= startDate != null ? startDate : "2023/01/01";
        endDate=endDate != null? endDate: "2025/01/01";
        // Tách chuỗi ngày tháng theo dấu "/"
        String[] startParts = startDate.split("/");
        String[] endParts = endDate.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);

        // Tạo đối tượng Date từ các giá trị đã tách
        Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date end = new Date(endYear - 1900, endMonth, endDay); // Năm cần giảm đi 1900
        // Kiểm tra giá trị Date đã được tạo thành công
        System.out.println("Start Date: " + start);
        System.out.println("End Date: " + end);
        MostPostAccountListResponse mostPostAccountListResponse = statisticService.getMostPostAccount(start, end);
        List<MostPostsAccountsDTO> chart4 = mostPostAccountListResponse.getMostPostAccountList();
        MostDonationEventListResponse mostDonationEventListResponse= statisticService.getMostDonationEvent(start,end);
        List<MostDonationEventsDTO> chart3=mostDonationEventListResponse.getMostDonationEventList();
        MostViewedPostListResponse mostViewedPostListResponse= statisticService.getMostViewedPosts(start,end);
        List<MostViewedPostsDTO> chart1=mostViewedPostListResponse.getMostViewedPostList();
        MostCharitableListResponse mostCharitableListResponse=statisticService.getMostCharitable(ce_id,start,end);
        List<MostCharitablePeopleDTO> chart2=mostCharitableListResponse.getMostCharitableList();

        List<CharityEvent> allCharities = new ArrayList<>();

        int totalPages = charityService.getAllCharities(0).getTotalPages();
                for (int page = 0; page < totalPages; page++) {
            CharityListResponse response = charityService.getAllCharities( page);
            allCharities.addAll(response.getCharityList());
        }

        CharityListResponse disburse= statisticService.disburseCharity(start,end);
        model.addAttribute("chart4", chart4);
        model.addAttribute("chart3",chart3);
        model.addAttribute("chart1",chart1);
        model.addAttribute("chart2",chart2);
        model.addAttribute("disburse",disburse.getCharityList());
        model.addAttribute("list",allCharities);
        model.addAttribute("startDate",start);
        model.addAttribute("endDate",end);
        model.addAttribute("id",ce_id);
        return "statistic";
    }
    @GetMapping("most_post_account")
    public String most_post_account(Model model,@PathParam("startDate") String startDate,
                                    @PathParam("endDate") String endDate){
        startDate= startDate != null ? startDate : "2023/01/01";
        endDate=endDate != null? endDate: "2025/01/01";
        // Tách chuỗi ngày tháng theo dấu "/"
        String[] startParts = startDate.split("/");
        String[] endParts = endDate.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);

        // Tạo đối tượng Date từ các giá trị đã tách
        Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date end = new Date(endYear - 1900, endMonth, endDay); // Năm cần giảm đi 1900
        MostPostAccountListResponse mostPostAccountListResponse = statisticService.getMostPostAccount(start, end);
        List<MostPostsAccountsDTO> data = mostPostAccountListResponse.getMostPostAccountList();
        model.addAttribute("data",data);
        model.addAttribute("startDate",start);
        model.addAttribute("endDate",end);
        model.addAttribute("start",startDate);
        model.addAttribute("end",endDate);
        return "statistic/most_post_account";
    }

    @GetMapping("most_post_account/export")
    public ResponseEntity<byte[]> exportToPDF(@Query("start") String start,@Query("end") String end, Model model, HttpServletResponse response) throws IOException {
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
        String htmlContent = MostPostAccountPDF(start,end);

        // Sử dụng HtmlConverter để chuyển HTML thành PDF với font Times New Roman
        HtmlConverter.convertToPdf(htmlContent, pdfDocument.getWriter());




        // Cấu hình header để trả về file PDF cho người dùng
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mostpostaccount.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");



        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
    private String MostPostAccountPDF(String start,String end) {
        String[] startParts = start.split("/");
        String[] endParts = end.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);
        Date startDate = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date endDate = new Date(endYear - 1900, endMonth, endDay);
        MostPostAccountListResponse mostPostAccountListResponse=statisticService.getMostPostAccount(startDate,endDate);
        List<MostPostsAccountsDTO> lists=mostPostAccountListResponse.getMostPostAccountList();



        // Tạo nội dung HTML
        StringBuilder htmlContent = new StringBuilder("<html><head><style>body { font-family: 'Times New Roman'; }</style></head><body>");
        htmlContent.append("<h1>Thống kê số bài viết của tài khoản</h1>");
        htmlContent.append("<p>Thời gian: từ ").append(start).append(" đến ").append(end).append("</p>");
        htmlContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        htmlContent.append("<thead><tr><th>ID</th><th>Tên tài khoản</th><th>Số bài viết</th></tr></thead>");
        htmlContent.append("<tbody>");

        // Duyệt qua danh sách sao kê và tạo các dòng trong bảng HTML

        for (MostPostsAccountsDTO list : lists) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(list.getAccountId()).append("</td>");
            htmlContent.append("<td>").append(list.getAccountName()).append("</td>");
            htmlContent.append("<td>").append(list.getPostCount()).append("</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }
    @GetMapping("disburse")
    public String disburse(Model model,@PathParam("startDate") String startDate,
                           @PathParam("endDate") String endDate){
        startDate= startDate != null ? startDate : "2023/01/01";
        endDate=endDate != null? endDate: "2025/01/01";
        // Tách chuỗi ngày tháng theo dấu "/"
        String[] startParts = startDate.split("/");
        String[] endParts = endDate.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);

        // Tạo đối tượng Date từ các giá trị đã tách
        Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date end = new Date(endYear - 1900, endMonth, endDay); // Năm cần giảm đi 1900
        CharityListResponse charityListResponse = statisticService.disburseCharity(start, end);
        List<CharityEvent> data = charityListResponse.getCharityList();
        model.addAttribute("data",data);
        model.addAttribute("startDate",start);
        model.addAttribute("endDate",end);
        model.addAttribute("start",startDate);
        model.addAttribute("end",endDate);
        return "statistic/disburse";
    }


    @GetMapping("disburse/export")
    public ResponseEntity<byte[]> disbursePDF(@Query("start") String start,@Query("end") String end, Model model, HttpServletResponse response) throws IOException {
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
        String htmlContent = disbursePDF(start,end);

        // Sử dụng HtmlConverter để chuyển HTML thành PDF với font Times New Roman
        HtmlConverter.convertToPdf(htmlContent, pdfDocument.getWriter());




        // Cấu hình header để trả về file PDF cho người dùng
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=disburse.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");



        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }







    private String disbursePDF(String start,String end) {
        String[] startParts = start.split("/");
        String[] endParts = end.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);
        Date startDate = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date endDate = new Date(endYear - 1900, endMonth, endDay);
        CharityListResponse charityListResponse=statisticService.disburseCharity(startDate,endDate);
        List<CharityEvent> lists=charityListResponse.getCharityList();



        // Tạo nội dung HTML
        StringBuilder htmlContent = new StringBuilder("<html><head><style>body { font-family: 'Times New Roman'; }</style></head><body>");
        htmlContent.append("<h1>Thống kê giải ngân</h1>");
        htmlContent.append("<p>Thời gian: từ ").append(start).append(" đến ").append(end).append("</p>");
        htmlContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        htmlContent.append("<thead><tr><th>ID</th><th>Tên dự án</th><th>Mô tả</th><th>Ngày bắt đầu</th><th>Ngày kết thúc</th><th>Mục tiêu</th><th>Đạt được</th></tr></thead>");
        htmlContent.append("<tbody>");

        // Duyệt qua danh sách sao kê và tạo các dòng trong bảng HTML

        for (CharityEvent list : lists) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(list.getId()).append("</td>");
            htmlContent.append("<td>").append(list.getName()).append("</td>");
            htmlContent.append("<td>").append(list.getDescription()).append("</td>");
            htmlContent.append("<td>").append(list.getStartTime()).append("</td>");
            htmlContent.append("<td>").append(list.getEndTime()).append("</td>");
            htmlContent.append("<td>").append(list.getGoalAmount()).append(" VND</td>");
            htmlContent.append("<td>").append(list.getCurrentAmount()).append(" VND</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }
    @GetMapping("most_donate_events")
    public String most_donate_event(Model model,@PathParam("startDate") String startDate,
                                    @PathParam("endDate") String endDate){
        startDate= startDate != null ? startDate : "2023/01/01";
        endDate=endDate != null? endDate: "2025/01/01";
        // Tách chuỗi ngày tháng theo dấu "/"
        String[] startParts = startDate.split("/");
        String[] endParts = endDate.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);

        // Tạo đối tượng Date từ các giá trị đã tách
        Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date end = new Date(endYear - 1900, endMonth, endDay); // Năm cần giảm đi 1900
        MostDonationEventListResponse mostDonationEventListResponse= statisticService.getMostDonationEvent(start, end);
        List<MostDonationEventsDTO> data =mostDonationEventListResponse.getMostDonationEventList();
        model.addAttribute("data",data);
        model.addAttribute("startDate",start);
        model.addAttribute("endDate",end);
        model.addAttribute("start",startDate);
        model.addAttribute("end",endDate);
        return "statistic/most_donate_event";
    }



    private String MostDonateEventPDF(String start,String end) {
        String[] startParts = start.split("/");
        String[] endParts = end.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);
        Date startDate = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date endDate = new Date(endYear - 1900, endMonth, endDay);
        MostDonationEventListResponse charityListResponse=statisticService.getMostDonationEvent(startDate,endDate);
        List<MostDonationEventsDTO> lists=charityListResponse.getMostDonationEventList();



        // Tạo nội dung HTML
        StringBuilder htmlContent = new StringBuilder("<html><head><style>body { font-family: 'Times New Roman'; }</style></head><body>");
        htmlContent.append("<h1>Dự án được ủng hộ nhiều nhất</h1>");
        htmlContent.append("<p>Thời gian: từ ").append(start).append(" đến ").append(end).append("</p>");
        htmlContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        htmlContent.append("<thead><tr><th>ID</th><th>Tên dự án</th><th>Số tiền mục tiêu</th><th>Số tiền hiện tại</th></tr></thead>");
        htmlContent.append("<tbody>");

        // Duyệt qua danh sách sao kê và tạo các dòng trong bảng HTML

        for (MostDonationEventsDTO list : lists) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(list.getCharityEventId()).append("</td>");
            htmlContent.append("<td>").append(list.getName()).append("</td>");
            htmlContent.append("<td>").append(list.getGoalAmount()).append(" VND</td>");
            htmlContent.append("<td>").append(list.getTotalDonationAmount()).append(" VND</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }

    @GetMapping("most_donate_events/export")
    public ResponseEntity<byte[]> mostdonateeventPDF(@Query("start") String start,@Query("end") String end, Model model, HttpServletResponse response) throws IOException {
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
        String htmlContent = MostDonateEventPDF(start,end);

        // Sử dụng HtmlConverter để chuyển HTML thành PDF với font Times New Roman
        HtmlConverter.convertToPdf(htmlContent, pdfDocument.getWriter());




        // Cấu hình header để trả về file PDF cho người dùng
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=most_donate_events.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");



        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


    @GetMapping("most_viewed_posts")
    public String most_viewed_posts(Model model,@PathParam("startDate") String startDate,
                                    @PathParam("endDate") String endDate){
        startDate= startDate != null ? startDate : "2023/01/01";
        endDate=endDate != null? endDate: "2025/01/01";
        // Tách chuỗi ngày tháng theo dấu "/"
        String[] startParts = startDate.split("/");
        String[] endParts = endDate.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);

        // Tạo đối tượng Date từ các giá trị đã tách
        Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date end = new Date(endYear - 1900, endMonth, endDay); // Năm cần giảm đi 1900
        MostViewedPostListResponse mostDonationEventListResponse= statisticService.getMostViewedPosts(start, end);
        List<MostViewedPostsDTO> data =mostDonationEventListResponse.getMostViewedPostList();
        model.addAttribute("data",data);
        model.addAttribute("startDate",start);
        model.addAttribute("endDate",end);
        model.addAttribute("start",startDate);
        model.addAttribute("end",endDate);
        return "statistic/most_viewed_post";
    }

    private String MostViewedPostsPDF(String start,String end) {
        String[] startParts = start.split("/");
        String[] endParts = end.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);
        Date startDate = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date endDate = new Date(endYear - 1900, endMonth, endDay);
        MostViewedPostListResponse charityListResponse=statisticService.getMostViewedPosts(startDate,endDate);
        List<MostViewedPostsDTO> lists=charityListResponse.getMostViewedPostList();



        // Tạo nội dung HTML
        StringBuilder htmlContent = new StringBuilder("<html><head><style>body { font-family: 'Times New Roman'; }</style></head><body>");
        htmlContent.append("<h1>Dự án được ủng hộ nhiều nhất</h1>");
        htmlContent.append("<p>Thời gian: từ ").append(start).append(" đến ").append(end).append("</p>");
        htmlContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        htmlContent.append("<thead><tr><th>ID</th><th>Tên bài viết</th><th>Lượt xem</th></tr></thead>");
        htmlContent.append("<tbody>");

        // Duyệt qua danh sách sao kê và tạo các dòng trong bảng HTML

        for (MostViewedPostsDTO list : lists) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(list.getPostId()).append("</td>");
            htmlContent.append("<td>").append(list.getTitle()).append("</td>");
            htmlContent.append("<td>").append(list.getCount()).append("</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }

    @GetMapping("most_viewed_posts/export")
    public ResponseEntity<byte[]> mostviewedpostPDF(@Query("start") String start,@Query("end") String end, Model model, HttpServletResponse response) throws IOException {
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
        String htmlContent = MostViewedPostsPDF(start,end);

        // Sử dụng HtmlConverter để chuyển HTML thành PDF với font Times New Roman
        HtmlConverter.convertToPdf(htmlContent, pdfDocument.getWriter());




        // Cấu hình header để trả về file PDF cho người dùng
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=most_viewed_posts.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");



        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping("most_charitable_person")
    public String mostCharitablePerson(Model model,
                                    @PathParam("ce_id") UUID ce_id,
                                    @PathParam("startDate") String startDate,
                                    @PathParam("endDate") String endDate) {
        ce_id = ce_id !=null? ce_id : UUID.fromString("37313236-6663-3238-2d64-3633322d3461");
        String name=charityService.getCharityById(ce_id).getName();
        startDate= startDate != null ? startDate : "2023/01/01";
        endDate=endDate != null? endDate: "2025/01/01";
        // Tách chuỗi ngày tháng theo dấu "/"
        String[] startParts = startDate.split("/");
        String[] endParts = endDate.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);

        // Tạo đối tượng Date từ các giá trị đã tách
        Date start = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date end = new Date(endYear - 1900, endMonth, endDay); // Năm cần giảm đi 1900
        // Kiểm tra giá trị Date đã được tạo thành công
        System.out.println("Start Date: " + start);
        System.out.println("End Date: " + end);
        MostCharitableListResponse mostCharitableListResponse=statisticService.getMostCharitable(ce_id,start,end);
        List<MostCharitablePeopleDTO> chart2=mostCharitableListResponse.getMostCharitableList();

        List<CharityEvent> allCharities = new ArrayList<>();

        int totalPages = charityService.getAllCharities(0).getTotalPages();
        for (int page = 0; page < totalPages; page++) {
            CharityListResponse response = charityService.getAllCharities( page);
            allCharities.addAll(response.getCharityList());
        }

        CharityListResponse disburse= statisticService.disburseCharity(start,end);
        model.addAttribute("data",chart2);
        model.addAttribute("list",allCharities);
        model.addAttribute("startDate",start);
        model.addAttribute("endDate",end);
        model.addAttribute("start",startDate);
        model.addAttribute("end",endDate);
        model.addAttribute("id",ce_id);
        return "statistic/most_charitable_person";
    }

    private String MostCharitablePersonPDF(UUID id,String start,String end) {
        String[] startParts = start.split("/");
        String[] endParts = end.split("/");

        // Lấy năm, tháng, ngày từ các chuỗi đã tách
        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0 (0 = January)
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]) - 1; // Tháng trong `Date` bắt đầu từ 0
        int endDay = Integer.parseInt(endParts[2]);
        Date startDate = new Date(startYear - 1900, startMonth, startDay); // Năm cần giảm đi 1900
        Date endDate = new Date(endYear - 1900, endMonth, endDay);
        MostCharitableListResponse charityListResponse=statisticService.getMostCharitable(id,startDate,endDate);
        List<MostCharitablePeopleDTO> lists=charityListResponse.getMostCharitableList();
        CharityEvent c=charityService.getCharityById(id);


        // Tạo nội dung HTML
        StringBuilder htmlContent = new StringBuilder("<html><head><style>body { font-family: 'Times New Roman'; }</style></head><body>");
        htmlContent.append("<h1>Người ủng hộ nhiều nhất cho 1 dự án</h1>");
        htmlContent.append("<p>ID: ").append(c.getId()).append("</p>");
        htmlContent.append("<p>Tên dự án: ").append(c.getName()).append("</p>");
        htmlContent.append("<p>Thời gian: từ ").append(start).append(" đến ").append(end).append("</p>");
        htmlContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        htmlContent.append("<thead><tr><th>ID</th><th>Tên bài viết</th><th>Lượt xem</th></tr></thead>");
        htmlContent.append("<tbody>");

        // Duyệt qua danh sách sao kê và tạo các dòng trong bảng HTML

        for (MostCharitablePeopleDTO list : lists) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(list.getTs_id()).append("</td>");
            htmlContent.append("<td>").append(list.getName()).append("</td>");
            htmlContent.append("<td>").append(list.getAmount()).append("</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody>");
        htmlContent.append("</table>");
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }

    @GetMapping("most_charitable_person/export")
    public ResponseEntity<byte[]> mostcharitablepersonPDF(@Query("id") UUID id,@Query("start") String start,@Query("end") String end, Model model, HttpServletResponse response) throws IOException {
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
        String htmlContent = MostCharitablePersonPDF(id,start,end);

        // Sử dụng HtmlConverter để chuyển HTML thành PDF với font Times New Roman
        HtmlConverter.convertToPdf(htmlContent, pdfDocument.getWriter());




        // Cấu hình header để trả về file PDF cho người dùng
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=most_charitable_person.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");



        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
}

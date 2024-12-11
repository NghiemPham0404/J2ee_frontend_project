package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.config.JwtProvider;
import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.AccountListResponse;
import com.example.j2ee_frontend_test.services.AccountService;
import com.example.j2ee_frontend_test.services.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;
    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping
    public String viewAccountsPage(Model model, @PathParam("page") Integer page) {
        page = page != null ? page : 0;
        AccountListResponse accountListResponse = accountService.getAllAccounts(1, page);
        if(accountListResponse!=null){

        }
        model.addAttribute("data", accountListResponse.getAccountList());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", accountListResponse.getTotalPages());
        model.addAttribute("total_results", accountListResponse.getTotalResults());

        accountListResponse.getAccountList().forEach(account -> System.out.println(account.getName()));

        List<Role> roles = roleService.getAllRoles();
        roles.forEach(role -> System.out.println(role.getName()));
        model.addAttribute("roles", roles);
        model.addAttribute("create", jwtProvider.containAuthority("Account Management create"));
        return "accounts";  // Thymeleaf template
    }

    @GetMapping("/new")
    public String showNewAccountForm(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);

        List<Role> roles = roleService.getAllRoles();
//        roles.forEach(role -> System.out.println(role.getName()));
        model.addAttribute("roles", roles);

        return "new_account";
    }

    @PostMapping("/save")
    public String saveAccount(@ModelAttribute("account") Account account) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Role role = roleService.getRoleById(account.getRole_id());
        account.setRole(role);
        account.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(account.getBirthDateAsString("yyyy-MM-dd")));
        String jString = objectMapper.writeValueAsString(account);
        System.out.println(jString);
        if (account.getId() != null) {
            accountService.updateAccount(account.getId(), account);
        } else {
            accountService.createAccount(account);
        }

        return "redirect:/account";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Account account = accountService.getAccountById(id);
        model.addAttribute("account", account);
        System.out.println("role id = "+account.getRole().getId());

        List<Role> roles = roleService.getAllRoles();
        roles.forEach(role -> System.out.println(role.getName()));
        model.addAttribute("roles", roles);
        model.addAttribute("update", jwtProvider.containAuthority("Account Management update"));
        model.addAttribute("delete", jwtProvider.containAuthority("Account Management delete"));
        return "update_account";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable("id") Integer id){
        accountService.deleteAccount(id);
        return "redirect:/account";
    }

//    @PostMapping("/upload")
//    public String uploadExcelFile(@RequestParam("file") MultipartFile file, Model model) {
//        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
//            model.addAttribute("error", "Invalid file format. Please upload an Excel file.");
//            return "error";
//        }
//
//        try {
//            List<Account> accounts = new ArrayList<>();
//            Workbook workbook = new XSSFWorkbook(file.getInputStream());
//            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
//
//            // Duyệt qua các dòng trong sheet, bỏ qua dòng đầu tiên (header)
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row != null) {
//                    Account account = new Account();
//                    account.setName(row.getCell(0).getStringCellValue()); // Cột tên
//                    account.setEmail(row.getCell(1).getStringCellValue()); // Cột email
//                    account.setPassword(row.getCell(2).getStringCellValue()); // Cột mật khẩu
//                    account.setRole(roleService.getRoleById(account.getRole_id()));
//
//                    String birthDateStr = row.getCell(4).getStringCellValue(); // Cột ngày sinh
//                    Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateStr);
//                    account.setBirthDate(birthDate);
//
//                    accounts.add(account);
//                }
//            }
//
//            workbook.close();
////            accountService.getAccountById();
//            model.addAttribute("success", "Thêm tài khoản thành công!");
//            return "redirect:/account";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Error processing file: " + e.getMessage());
//            return "error";
//        }
//    }

    @PostMapping("/import")
    public String importAccountsFromExcel(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            model.addAttribute("error", "Invalid file format. Please upload an Excel file.");
            return "error";
        }

        try {
            List<Account> accounts = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Đọc từng dòng trong Excel
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ qua header
                Row row = sheet.getRow(i);
                if (row != null) {
                    Account account = new Account();
                    account.setName(row.getCell(0).getStringCellValue()); // Tên
                    Date birthDateStr = row.getCell(1).getDateCellValue();
                    account.setBirthDate(birthDateStr);
//                    account.setBirthDate(new SimpleDateFormat("dd/MM/YYYY").parse(birthDateStr));
                    account.setEmail(row.getCell(2).getStringCellValue()); // Email
                    account.setUsername(row.getCell(3).getStringCellValue());
                    account.setPassword(row.getCell(4).getStringCellValue()); // Mật khẩu
                    account.setActive(true);

                    int roleId = (int) row.getCell(5).getNumericCellValue();
                    account.setRole_id(roleId);
                    Role role = roleService.getRoleById(roleId);
                    account.setRole(role);

                    accounts.add(account);
                }
            }

            workbook.close();

            // Gửi danh sách tài khoản tới API
            for (Account account : accounts) {
                accountService.createAccount(account);
            }

            model.addAttribute("success", "Thêm thành công!");
            return "redirect:/account";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to process file: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportAccountsToExcel() {
        try {
            // Gọi service để lấy danh sách tài khoản và tạo file Excel
            ByteArrayResource excelFile = accountService.exportAccountsToExcel();

            // Trả file Excel về phía client
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=accounts.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelFile);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}


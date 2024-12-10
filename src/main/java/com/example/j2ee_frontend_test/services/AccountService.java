package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.response.AccountListResponse;
import com.example.j2ee_frontend_test.services.apis.AccountApi;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountApi accountApi;


    public AccountListResponse getAllAccounts(int adminId, int page) {
        Call<AccountListResponse> call = accountApi.getAllAccounts(adminId, page);
        Response<AccountListResponse> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                // Handle errors here, if needed
                System.out.println("Error: " + response.errorBody());
                return new AccountListResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public AccountListResponse SearchNameAccounts(String query, int page) {
        Call<AccountListResponse> call = accountApi.searchNameAccounts(query, page);
        Response<AccountListResponse> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                // Handle errors here, if needed
                System.out.println("Error: " + response.errorBody());
                return new AccountListResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String createAccount(Account account) {
        Call<ResponseEntity<Object>> call = accountApi.createAccount(account);
        try {
            Response<ResponseEntity<Object>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Account getAccountById(Integer id) {
        Call<Account> call = accountApi.getAccountById(id);
        try {
            Response<Account> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateAccount(Integer id, Account account) {
        try {
            Response<ResponseEntity<Object>> response = accountApi.updateAccount(id, account).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return response.errorBody().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteAccount(Integer id) {
        try {
            Response<ResponseEntity<Object>> response = accountApi.deleteAccount(id).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().toString();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return response.errorBody().toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ByteArrayResource exportAccountsToExcel() {
        try {
            // Lấy danh sách tài khoản từ API
            List<Account> accounts = getAllAccounts(1, 0).getAccountList();

            // Tạo workbook Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Accounts");

            // Tạo header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Tên đăng nhập", "Tên", "Email", "Vai trò", "Ngày sinh"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Điền dữ liệu vào các dòng tiếp theo
            int rowIdx = 1;
            for (Account account : accounts) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(account.getId() != null ? account.getId() : 0);
                row.createCell(1).setCellValue(account.getUsername() != null ? account.getUsername() : ""); // Thêm tên đăng nhập
                row.createCell(2).setCellValue(account.getName() != null ? account.getName() : "");
                row.createCell(3).setCellValue(account.getEmail() != null ? account.getEmail() : "");
                row.createCell(4).setCellValue(account.getRole() != null ? account.getRole().getName() : "Chưa xác định");
                row.createCell(5).setCellValue(
                        account.getBirthDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(account.getBirthDate()) : ""
                );
            }

            // Ghi dữ liệu ra OutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            return new ByteArrayResource(outputStream.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage());
        }
    }


}

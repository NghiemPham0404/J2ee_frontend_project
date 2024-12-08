package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.Role;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.j2ee_frontend_test.models.Account;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {


    public void importExcelData(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            List<Account> accounts = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua dòng tiêu đề

                Account account = new Account();
                account.setId((int) row.getCell(0).getNumericCellValue());
                account.setName(row.getCell(1).getStringCellValue());
                account.setEmail(row.getCell(2).getStringCellValue());
                account.setUsername(row.getCell(3).getStringCellValue());
                account.setRole(getRoleFromExcel(row.getCell(4).getStringCellValue())); // Xử lý dữ liệu quyền
                account.setActive(row.getCell(5).getBooleanCellValue()); // Xử lý dữ liệu trạng thái
                accounts.add(account);
            }

            userRepository.saveAll(accounts); // Lưu vào cơ sở dữ liệu
        }
    }

    private Role getRoleFromExcel(String roleName) {
        // Tìm kiếm role từ tên role trong Excel (cần có logic tùy chỉnh cho trường hợp này)
        return new Role(roleName);
    }
}

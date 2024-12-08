package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.Repository.roleRepository;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.Role;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {


    private Object userRepository;

    @Autowired
    private roleRepository roleRepository;

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

            userRepository.equals(accounts); // Lưu vào cơ sở dữ liệu
        }
    }

    private Role getRoleFromExcel(String roleName) {
        return com.example.j2ee_frontend_test.Repository.roleRepository.findByName(roleName).orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(roleName);
            return roleRepository.save(newRole);
        });
    }

}

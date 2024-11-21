package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.AccountListResponse;
import com.example.j2ee_frontend_test.services.AccountService;
import com.example.j2ee_frontend_test.services.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;

    @GetMapping
    public String viewAccountsPage(Model model, @PathParam("page") Integer page) {
        page = page != null ? page - 1 : 0;
        AccountListResponse accountListResponse = accountService.getAllAccounts(1, page);
        model.addAttribute("data", accountListResponse.getAccountList());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", accountListResponse.getTotalPages());
        model.addAttribute("total_results", accountListResponse.getTotalResults());

        accountListResponse.getAccountList().forEach(account -> System.out.println(account.getName()));

        List<Role> roles = roleService.getAllRoles();
        roles.forEach(role -> System.out.println(role.getName()));
        model.addAttribute("roles", roles);
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
        System.out.println("role id = " + account.getRole().getId());

        List<Role> roles = roleService.getAllRoles();
        roles.forEach(role -> System.out.println(role.getName()));
        model.addAttribute("roles", roles);

        return "update_account";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable("id") Integer id) {
        accountService.deleteAccount(id);
        return "redirect:/account";
    }
}
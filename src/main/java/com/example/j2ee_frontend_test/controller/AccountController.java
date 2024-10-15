package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.config.RetrofitClientConfig;
import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.response.AccountResponse;
import com.example.j2ee_frontend_test.services.AccountApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountApi accountApi;


    @GetMapping
    public String viewAccountsPage(Model model) throws IOException {
        Call<AccountResponse> call = accountApi.getAllAccounts(1, 1);
        Response<AccountResponse> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            List<Account> accounts = response.body().getAccountList();
            model.addAttribute("data", accounts);
        } else {
            // Handle errors here, if needed
            System.out.println("Error: " + response.errorBody());
        }

        return "accounts";  // Thymeleaf template
    }

    @GetMapping("/new")
    public String showNewAccountForm(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "new_account";
    }

    @PostMapping("/save")
    public String saveAccount(@ModelAttribute("account") Account account) throws IOException {
        if (account.getId() != null) {
            accountApi.updateAccount(account.getId(), account).execute();
        } else {
            accountApi.createAccount(account).execute();
        }

        return "redirect:/accounts";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws IOException {
        Call<Account> call = accountApi.getAccountById(id);
        Response<Account> response = call.execute();

        if (response.isSuccessful()) {
            Account account = response.body();
            model.addAttribute("account", account);
        }

        return "update_account";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable("id") Integer id) throws IOException {
        accountApi.deleteAccount(id).execute();
        return "redirect:/accounts";
    }
}


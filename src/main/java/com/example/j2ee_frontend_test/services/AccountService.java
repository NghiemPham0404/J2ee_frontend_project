package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.response.AccountListResponse;
import com.example.j2ee_frontend_test.services.apis.AccountApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

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
}

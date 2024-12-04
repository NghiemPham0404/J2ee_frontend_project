package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.models.CharityEvent;
import com.example.j2ee_frontend_test.response.CharityListResponse;
import com.example.j2ee_frontend_test.services.apis.CharityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.UUID;

@Service
public class CharityService {
    @Autowired
    CharityApi charityApi;

    public CharityListResponse getAllCharities( int page) {
        Call<CharityListResponse> call = charityApi.getAllCharities( page);
        Response<CharityListResponse> response = null;
        try {
            response = call.execute();
            System.out.println(response.body());
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody());
                return new CharityListResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CharityListResponse searchNameCharities(String query, int page) {
        Call<CharityListResponse> call = charityApi.searchNameCharities(query, page);
        Response<CharityListResponse> response = null;
        try {
            response = call.execute();
            System.out.println(response.body());
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody());
                return new CharityListResponse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String createCharity(CharityEvent charityEvent) {
        Call<ResponseEntity<Object>> call = charityApi.createCharity(charityEvent);
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

    public CharityEvent getCharityById(UUID id) {
        Call<CharityEvent> call = charityApi.getCharityById(id);
        try {
            Response<CharityEvent> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateCharity(UUID id, CharityEvent charityEvent) {
        Call<ResponseEntity<Object>> call = charityApi.updateCharity(id, charityEvent);
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

    public String deleteCharity(UUID id) {
        Call<ResponseEntity<Object>> call = charityApi.deleteCharity(id);
        try {            Response<ResponseEntity<Object>> response = call.execute();
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
    public String disburseCharity(UUID id) {
        Call<ResponseEntity<Object>> call = charityApi.disburse(id);
        try {            Response<ResponseEntity<Object>> response = call.execute();
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
    public CharityListResponse ongoingCharity(int page) {
        Call<CharityListResponse> call = charityApi.searchOngoing(page);
        try {            Response<CharityListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CharityListResponse notDisburseCharity(int page) {
        Call<CharityListResponse> call = charityApi.searchNotDisburse(page);
        try {            Response<CharityListResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Success");
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



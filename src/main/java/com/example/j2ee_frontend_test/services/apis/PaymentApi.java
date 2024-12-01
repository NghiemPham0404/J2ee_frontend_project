package com.example.j2ee_frontend_test.services.apis;


import com.example.j2ee_frontend_test.models.Payment;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

public interface PaymentApi  {

    // Find all payments by display status
    List<Payment> findByDisplay(int display);



}


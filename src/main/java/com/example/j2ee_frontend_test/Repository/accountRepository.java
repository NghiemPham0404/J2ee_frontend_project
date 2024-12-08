package com.example.j2ee_frontend_test.Repository;

import com.example.j2ee_frontend_test.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface accountRepository extends JpaRepository<Account, Integer> {
}

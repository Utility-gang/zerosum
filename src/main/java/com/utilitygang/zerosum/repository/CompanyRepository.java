package com.utilitygang.zerosum.repository;

import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, String> {
    boolean existsBySymbolIgnoreCase(String symbol);

    interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsernameIgnoreCase(String username);

        boolean existsByUsernameIgnoreCase(String username);
    }
}

package com.utilitygang.zerosum.repository;

import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.model.Stock;
import com.utilitygang.zerosum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByOwner(User owner);

    List<Stock> findByOwnerId(Long ownerId);

    List<Stock> findByCompany(Company company);

    Optional<Stock> findByOwnerIdAndCompanySymbol(Long ownerId, String symbol);

    boolean existsByOwnerIdAndCompanySymbol(Long ownerId, String symbol);
}

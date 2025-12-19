package com.utilitygang.zerosum.repository;

import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, String> {
    boolean existsBySymbolIgnoreCase(String symbol);

    @Query("""
                select distinct s.company
                from Stock s
                where s.owner = :owner
            """)
    List<Company> findAllByStockOwner(@Param("owner") User owner);
}

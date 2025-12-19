package com.utilitygang.zerosum.repository;

import com.utilitygang.zerosum.model.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketPriceRepository extends JpaRepository<MarketPrice, String> {
}


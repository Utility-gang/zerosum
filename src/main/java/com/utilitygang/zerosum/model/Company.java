package com.utilitygang.zerosum.model;

import com.utilitygang.zerosum.data.PriceData;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "companies")
public class Company {
    @Id
    private String symbol;

    private String logo;

    @Transient
    private BigDecimal currPrice;

    public Company(String symbol, String logo) {
        this.symbol = symbol;
        this.logo = logo;
        this.currPrice = PriceData.getPrice(symbol);
    }
}

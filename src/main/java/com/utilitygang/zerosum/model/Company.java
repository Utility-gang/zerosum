package com.utilitygang.zerosum.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "companies")
public class Company {
    @Id
    private String symbol;

    private String logo;

    public Company(String symbol, String logo) {
        this.symbol = symbol;
        this.logo = logo;
    }
}

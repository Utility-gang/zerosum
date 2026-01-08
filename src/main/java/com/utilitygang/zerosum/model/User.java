package com.utilitygang.zerosum.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // BigDecimal is the best to use for currency apparently
    private BigDecimal cash;

    private BigDecimal portfolioValue;

    @Transient
    private BigDecimal loss;

    @Transient
    private Float percentageLoss;

    public User(String username) {
        this.username = username;
        this.cash = new BigDecimal(5000.0);
        this.portfolioValue = new BigDecimal(5000.0);
        this.loss = new BigDecimal(0.0);
        this.percentageLoss = new Float(0.0);
    }

    public User() {
    }
}

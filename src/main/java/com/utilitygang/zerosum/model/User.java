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

    public User(String username) {
        this.username = username;
        this.cash = new BigDecimal(50000.0);
    }
}

package com.utilitygang.zerosum.Model;

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

    // BigDecimal is the closest to sql's DECIMAL apparently
    private BigDecimal cash;
}
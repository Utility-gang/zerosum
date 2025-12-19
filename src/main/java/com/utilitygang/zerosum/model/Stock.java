package com.utilitygang.zerosum.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id")
    private Company company;

    public Stock(Double amount, User owner, Company company) {
        this.amount = amount;
        this.owner = owner;
        this.company = company;
    }

    public Stock() {
    }
}

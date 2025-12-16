package com.utilitygang.zerosum.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}

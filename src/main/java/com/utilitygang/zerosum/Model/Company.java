package com.utilitygang.zerosum.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "company")
public class Company {
    @Id
    private String symbol;

    private String logo;
}

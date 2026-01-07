package com.utilitygang.zerosum.model;

import com.utilitygang.zerosum.data.PriceData;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name = "companies")
public class Company {
    @Id
    private String symbol;

    private String logo;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "tone_tag")
    private String toneTag;
    
    @Column(name = "cached_price")
    private BigDecimal cachedPrice;

    public Company() {
    };

    @Transient
    private BigDecimal currPrice;

    public Company(String symbol, String logo) {
        this.symbol = symbol;
        this.logo = logo;
        this.currPrice = PriceData.getPrice(symbol);
    }

    //override the lombok getter for price so that it returns
    //the live price if we have one and if not, returns the cached price
    public BigDecimal getCurrPrice() {
        BigDecimal livePrice = PriceData.getPrice(this.symbol);

        //signum returns 1 or -1 if the number is pos/neg but will
        // return 0 if number is zero regardless of scale
        if (livePrice != null && livePrice.signum() != 0) {
            return livePrice;
        }

        return (cachedPrice != null) ? cachedPrice : BigDecimal.ZERO;
    }
}

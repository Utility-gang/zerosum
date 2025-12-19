package com.utilitygang.zerosum.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "market_price")
public class MarketPrice {

    @Id
    private String symbol;

    private double price;

    private Instant lastUpdated;

    @Enumerated(EnumType.STRING)
    private PriceSource source;

    public MarketPrice() {}

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }

    public PriceSource getSource() { return source; }
    public void setSource(PriceSource source) { this.source = source; }
}

package com.utilitygang.zerosum.Data;

import java.util.*;

public class PriceData {
    private static final Map<String, Double> prices = new HashMap<>();

    public static void setPrice(String ticker, Double price) {
        if (ticker != null && price != null) {
            prices.put(ticker, price);
        }
    }

    public static Double getPrice(String ticker) {
        return prices.get(ticker);
    }
}

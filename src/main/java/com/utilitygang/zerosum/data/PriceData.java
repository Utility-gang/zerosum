package com.utilitygang.zerosum.data;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PriceData {
    // using a 'ConcurrentHashMap' data structure here instead of a 'HashMap'
    // as its thread safe when doing lots of concurrent reads/writes, imagine
    // it utilises some sort of lock
    // https://www.baeldung.com/java-concurrent-map
    private static final Map<String, BigDecimal> prices = new ConcurrentHashMap<>();

    public static void setPrice(String symbol, BigDecimal price) {
        if (symbol != null && price != null) {
            prices.put(symbol, price);
        }
    }

    // 'getOrDefault' method is recommended instead of 'get' for 'ConcurrentHashMap'
    // because you always have a value to read even if a write has removed the value
    // a millisecond before you try to access it
    public static BigDecimal getPrice(String symbol) {
        return prices.getOrDefault(symbol, new BigDecimal(0.0));
    }

    public static BigDecimal getPriceForStockAmount(String symbol, Double quantity) {
        Double pricePerUnit = getPrice(symbol).doubleValue();
        Double total = pricePerUnit * quantity;

        return new BigDecimal(total);
    }
}

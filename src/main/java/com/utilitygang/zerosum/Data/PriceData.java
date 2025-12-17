package com.utilitygang.zerosum.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PriceData {
    // using a 'ConcurrentHashMap' data structure here instead of a 'HashMap'
    // as its thread safe when doing lots of concurrent reads/writes, imagine
    // it utilises some sort of lock
    // https://www.baeldung.com/java-concurrent-map
    private static final Map<String, Double> prices = new ConcurrentHashMap<>();

    public static void setPrice(String symbol, Double price) {
        if (symbol != null && price != null) {
            prices.put(symbol, price);
        }
    }

    // 'getOrDefault' method is recommended instead of 'get' for 'ConcurrentHashMap'
    // because you always have a value to read even if a write has removed the value
    // a millisecond before you try to access it
    public static Double getPrice(String symbol) {
        return prices.getOrDefault(symbol, 0.0);
    }
}

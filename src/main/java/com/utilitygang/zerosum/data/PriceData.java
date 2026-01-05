package com.utilitygang.zerosum.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;

public class PriceData {
    // using a 'ConcurrentHashMap' data structure here instead of a 'HashMap'
    // as its thread safe when doing lots of concurrent reads/writes, imagine
    // it utilises some sort of lock
    // https://www.baeldung.com/java-concurrent-map
    private static Map<String, BigDecimal> prices = new ConcurrentHashMap<>();

    public static void deserialise() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // this really wants me to handle the exceptions
            prices = mapper.readValue(Paths.get("prices.json").toFile(), new TypeReference<Map<String, BigDecimal>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @PreDestroy
    public static void serialise() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // this really wants me to handle the exceptions
            mapper.writeValue(Paths.get("prices.json").toFile(), new ConcurrentHashMap<String, BigDecimal>(prices));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

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

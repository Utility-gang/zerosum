package com.utilitygang.zerosum.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PriceData {
    // using a 'ConcurrentHashMap' data structure here instead of a 'HashMap'
    // as its thread safe when doing lots of concurrent reads/writes, imagine
    // it utilises some sort of lock
    // https://www.baeldung.com/java-concurrent-map
    private static Map<String, List<BigDecimal>> prices = new ConcurrentHashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Path file = Paths.get("prices.json");

    public static void deserialise() {

        try {
            // this really wants me to handle the exceptions
            prices = mapper.readValue(file.toFile(), new TypeReference<ConcurrentHashMap<String, List<BigDecimal>>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void serialise() {
        try {
            // this really wants me to handle the exceptions
            mapper.writeValue(file.toFile(), new ConcurrentHashMap<String, List<BigDecimal>>(prices));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void setPrice(String symbol, BigDecimal price) {
        prices.computeIfAbsent(symbol, k -> Collections.synchronizedList(new ArrayList<>())).add(price);
    }

    public static BigDecimal getPrice(String symbol) {
        List<BigDecimal> list = prices.get(symbol);
        if (list == null)
            return null;

        synchronized (list) {
            return list.isEmpty()
                    ? null
                    : list.get(list.size() - 1);
        }
    }

    public static BigDecimal getPriceForStockAmount(String symbol, Double quantity) {
        Double pricePerUnit = getPrice(symbol).doubleValue();
        Double total = pricePerUnit * quantity;

        return new BigDecimal(total);
    }
}

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
    public record Stock(long time, double value) {
    }

    private static Map<String, List<Stock>> prices = new ConcurrentHashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Path file = Paths.get("prices.json");

    public static void deserialise() {
        try {
            // this really wants me to handle the exceptions
            prices = mapper.readValue(file.toFile(), new TypeReference<ConcurrentHashMap<String, List<Stock>>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void serialise() {
        try {
            // this really wants me to handle the exceptions
            mapper.writeValue(file.toFile(), new ConcurrentHashMap<String, List<Stock>>(prices));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // slow since we need to deduplicate and order correctly
    public static void setStock(String symbol, Double price, Long time) {
        List<Stock> list = prices.computeIfAbsent(symbol, k -> Collections.synchronizedList(new ArrayList<>()));

        synchronized (list) {
            list.removeIf(s -> s.time() == time);
            list.add(new Stock(time, price));
            list.sort(Comparator.comparingLong(Stock::time));
        }
    }

    public static BigDecimal getPrice(String symbol) {
        List<Stock> list = prices.get(symbol);
        if (list == null)
            return new BigDecimal(1.0);

        synchronized (list) {
            return list.isEmpty()
                    ? new BigDecimal(1.0)
                    : new BigDecimal(list.get(list.size() - 1).value());
        }
    }

    public static List<Stock> getPrices(String symbol) {
        return prices.getOrDefault(symbol, Collections.emptyList());
    }

    public static BigDecimal getPriceForStockAmount(String symbol, Double quantity) {
        Double pricePerUnit = getPrice(symbol).doubleValue();
        Double total = pricePerUnit * quantity;

        return new BigDecimal(total);
    }
}

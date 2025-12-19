package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.model.MarketPrice;
import com.utilitygang.zerosum.model.PriceSource;
import com.utilitygang.zerosum.repository.MarketPriceRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MarketPriceService {

    private final MarketPriceRepository repo;
    private static final long STALE_SECONDS = 15;

    public MarketPriceService(MarketPriceRepository repo) {
        this.repo = repo;
    }

    public void updateFromWebSocket(String symbol, double price) {
        save(symbol, price, PriceSource.WEBSOCKET);
    }

    public void updateFromQuote(String symbol, double price) {
        MarketPrice existing = repo.findById(symbol).orElse(null);

        if (existing == null || isStale(existing)) {
            save(symbol, price, PriceSource.QUOTE);
        }
    }

    private void save(String symbol, double price, PriceSource source) {
        MarketPrice mp = new MarketPrice();
        mp.setSymbol(symbol);
        mp.setPrice(price);
        mp.setSource(source);
        mp.setLastUpdated(Instant.now());
        repo.save(mp);
    }

    private boolean isStale(MarketPrice mp) {
        return mp.getLastUpdated()
                .isBefore(Instant.now().minusSeconds(STALE_SECONDS));
    }
}

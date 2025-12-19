package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.data.PriceData;
import com.utilitygang.zerosum.model. Stock;
import com.utilitygang.zerosum.model. User;
import com.utilitygang.zerosum.repository. StockRepository;
import org. springframework.beans.factory.annotation. Autowired;
import org. springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private StockRepository stockRepository;

    /**
     * Calculates the total portfolio value for a user
     * Total = Cash + Sum of (stock quantity * current price)
     */
    public BigDecimal calculateTotalPortfolioValue(User user) {
        List<Stock> userStocks = stockRepository.findByOwner(user);

        BigDecimal totalStockValue = BigDecimal. ZERO;

        for (Stock stock : userStocks) {
            BigDecimal stockPrice = PriceData.getPrice(stock.getCompany().getSymbol());
            BigDecimal stockAmount = BigDecimal.valueOf(stock.getAmount());
            BigDecimal stockValue = stockPrice.multiply(stockAmount);
            totalStockValue = totalStockValue.add(stockValue);
        }

        // Total portfolio = cash + stock values
        return user.getCash().add(totalStockValue);
    }

    /**
     * Calculates just the total value of stocks (excluding cash)
     */
    public BigDecimal calculateTotalStockValue(User user) {
        List<Stock> userStocks = stockRepository.findByOwner(user);

        BigDecimal totalStockValue = BigDecimal.ZERO;

        for (Stock stock : userStocks) {
            BigDecimal stockPrice = PriceData. getPrice(stock.getCompany().getSymbol());
            BigDecimal stockAmount = BigDecimal.valueOf(stock.getAmount());
            BigDecimal stockValue = stockPrice.multiply(stockAmount);
            totalStockValue = totalStockValue.add(stockValue);
        }

        return totalStockValue;
    }
}
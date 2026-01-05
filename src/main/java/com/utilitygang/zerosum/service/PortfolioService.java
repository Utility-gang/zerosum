package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.data.PriceData;
import com.utilitygang.zerosum.model.Stock;
import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.utilitygang.zerosum.data.PriceData.getPriceForStockAmount;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private StockRepository stockRepository;

    /**
     * Calculates just the total value of stocks (excluding cash)
     */
    public BigDecimal calculateTotalStockValue(User user) {
        return stockRepository.findByOwner(user).stream()
                .map(stock -> getPriceForStockAmount(stock.getCompany().getSymbol(), stock.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the total portfolio value for a user
     * Total = Cash + Sum of (stock quantity * current price)
     */
    public BigDecimal calculateTotalPortfolioValue(User user) {
        // Total portfolio = cash + stock values
        return user.getCash().add(calculateTotalStockValue(user));
    }
}

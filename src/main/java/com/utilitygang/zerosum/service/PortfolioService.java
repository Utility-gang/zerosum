package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.data.PriceData;
import com.utilitygang.zerosum.model.Stock;
import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.StockRepository;
import com.utilitygang.zerosum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PortfolioService {
    // update the cached portfolio values every 5 minutes
    private static final long REFRESH_RATE = 10 * 60 * 500;

    // delay the cache update by 1 min after application startup
    private static final long INITIAL_DELAY =  10 * 60 * 100;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Calculates just the total value of stocks (excluding cash)
     */
    public BigDecimal calculateTotalStockValue(User user) {
        return stockRepository.findByOwner(user).stream()
                .map(stock -> PriceData.getPriceForStockAmount(stock.getCompany().getSymbol(), stock.getAmount()))
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

    // update a single user's protfolio value
    public void updateCachedPortfolioValue(User user) {
        BigDecimal newPortfolioValue = calculateTotalPortfolioValue(user);
        user.setPortfolioValue(newPortfolioValue);
        userRepository.save(user);
    }

    // every 5 minutes, update the cached ptf values for all users
    @Scheduled(fixedRate = REFRESH_RATE, initialDelay = INITIAL_DELAY)
    public void updateAllCachedPortfolioValues() {
        // get all users
        List<User> allUsers = userRepository.findAll();

        // update the cached ptf value for each one
        for (User user : allUsers) {
            updateCachedPortfolioValue(user);
        }
        System.out.println("updated cache value");
    }
}

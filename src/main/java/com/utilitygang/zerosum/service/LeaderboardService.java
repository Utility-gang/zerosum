package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LeaderboardService {
    private static final BigDecimal STARTING_CASH = new BigDecimal(5000.0);

    public List<User> calculateLosses(List<User> topUsers) {
        for (User user : topUsers) {
            BigDecimal portfolioValue = user.getPortfolioValue();
            BigDecimal absoluteLoss = calculateAbsoluteLoss(portfolioValue);
            Float percentageLoss = calculatePercentageLoss(absoluteLoss);

            user.setLoss(absoluteLoss);
            user.setPercentageLoss(percentageLoss);
        }

        return topUsers;
    }

    private BigDecimal calculateAbsoluteLoss(BigDecimal portfolioValue) {
        return portfolioValue.subtract(STARTING_CASH);
    }

    private Float calculatePercentageLoss(BigDecimal absoluteLoss) {
        BigDecimal percentageLoss = absoluteLoss
                .divide(STARTING_CASH, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

        return percentageLoss.floatValue();
    }
}

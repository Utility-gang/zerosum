package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.model.Stock;
import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.StockRepository;
import com.utilitygang.zerosum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserContextService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioService portfolioService;

    @Autowired
    StockRepository stockRepository;

    public User getUser(@AuthenticationPrincipal DefaultOidcUser principal) {
        String email = (String) principal.getAttributes().get("email");
        return userRepository.findUserByUsername(email).orElse(null);
    }

    public String getUserPortfolioValue(User user) {
        if (user != null) {
            BigDecimal totalPortfolioValue = portfolioService.calculateTotalPortfolioValue(user);
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            return currencyFormatter.format(totalPortfolioValue);
        } else {
            return "";
        }
    }

    public Map<String, Double> getUserHoldings(User user) {
        List<Stock> userStocks = stockRepository.findByOwnerId(user.getId());
        return userStocks.stream().collect(Collectors.toMap(
                stock -> stock.getCompany().getSymbol(),
                Stock::getAmount,
                (existing, replacement) -> existing
        ));
    }

}

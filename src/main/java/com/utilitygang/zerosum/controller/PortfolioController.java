package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.UserRepository;
import com. utilitygang.zerosum. service.PortfolioService;
import org.springframework.beans. factory.annotation.Autowired;
import org.springframework.security. core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework. stereotype.Controller;
import org. springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util. Locale;

@Controller
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/portfolio")
    public String portfolioIndex(Model model, @AuthenticationPrincipal DefaultOidcUser principal) {
        if (principal != null) {
            String email = (String) principal.getAttributes().get("email");
            User user = userRepository.findUserByUsername(email).orElse(null);

            if (user != null) {
                // Calculate portfolio values
                BigDecimal totalPortfolioValue = portfolioService. calculateTotalPortfolioValue(user);
                BigDecimal totalStockValue = portfolioService.calculateTotalStockValue(user);
                BigDecimal cashBalance = user.getCash();

                // Format as currency
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

                // Add to model for the view
                model.addAttribute("totalPortfolioValue", currencyFormatter.format(totalPortfolioValue));
                model.addAttribute("totalStockValue", currencyFormatter.format(totalStockValue));
                model.addAttribute("cashBalance", currencyFormatter.format(cashBalance));

                // Raw values (if you need them for calculations)
                model.addAttribute("totalPortfolioValueRaw", totalPortfolioValue);
                model.addAttribute("totalStockValueRaw", totalStockValue);
                model.addAttribute("cashBalanceRaw", cashBalance);

                model.addAttribute("user", user);
            }
        }

        return "portfolio/index";
    }
}
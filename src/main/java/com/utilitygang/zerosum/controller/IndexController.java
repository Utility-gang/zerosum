package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.model.Stock;
import com.utilitygang.zerosum.repository.CompanyRepository;
import com.utilitygang.zerosum.repository.UserRepository;
import com.utilitygang.zerosum.repository.StockRepository;
import com.utilitygang.zerosum.service.PortfolioService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import java.util.stream.Collectors;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
public class IndexController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioService portfolioService;

    @GetMapping({ "/", "/portfolio" })
    public String indexAndPortfolio(Model model, @AuthenticationPrincipal DefaultOidcUser principal,
            HttpServletRequest req) {

        Map<String, Double> holdings = new java.util.HashMap<>();

        // use this to do the couple of changes in logic
        boolean isRoot = req.getRequestURI().equals("/");
        if (isRoot)
            model.addAttribute("companies", companyRepository.findAll());

        // DEBUG: Log authentication status
        System.out.println("🔍 DEBUG: principal is " + (principal == null ? "NULL" : "NOT NULL"));

        // Add portfolio value for navbar
        if (principal != null) {
            String email = (String) principal.getAttributes().get("email");
            System.out.println("🔍 DEBUG: Email from principal: " + email);

            User user = userRepository.findUserByUsername(email).orElse(null);
            System.out.println("🔍 DEBUG: User from database: " + (user == null ? "NULL" : user.getUsername()));

            if (user != null) {
                List<Stock> userStocks = stockRepository.findByOwnerId(user.getId());
                holdings = userStocks.stream().collect(Collectors.toMap(
                        stock -> stock.getCompany().getSymbol(),
                        Stock::getAmount,
                        (existing, replacement) -> existing));

                // add all the companies that the user has bought
                if (!isRoot) {
                    model.addAttribute("companies", companyRepository.findAllByStockOwner(user));

                    // model.addAttribute("ownedStocks",
                    // stockRepository.findByOwnerId(user.getId()));
                    // model.addAttribute("ownedStocks",
                    // stockRepository.findByOwnerId(user.getId()).get(0).getAmount());
                }

                BigDecimal totalPortfolioValue = portfolioService.calculateTotalPortfolioValue(user);
                System.out.println("🔍 DEBUG: Total portfolio value: " + totalPortfolioValue);

                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
                model.addAttribute("totalPortfolioValue", currencyFormatter.format(totalPortfolioValue));
                model.addAttribute("user", user);
            } else {
                System.out.println("⚠️ DEBUG: User not found in database for email: " + email);
            }
        } else {
            System.out.println("⚠️ DEBUG: Principal is null - user not authenticated");
        }

        model.addAttribute("holdings", holdings);

        return "index";
    }
}

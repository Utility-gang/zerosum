package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.NewsArticle;
import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.UserRepository;
import com.utilitygang.zerosum.service.FinnhubService;
import com.utilitygang.zerosum.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
public class NewsController {
    @Autowired
    FinnhubService finnhubService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioService portfolioService;

    @GetMapping({ "/news" })
    public String news(Model model, @AuthenticationPrincipal DefaultOidcUser principal) throws Exception {
        List<NewsArticle> newsArticles = finnhubService.fetchNews();
        model.addAttribute("newsArticles", newsArticles);

        // DEBUG: Log authentication status
        System.out.println("🔍 DEBUG: principal is " + (principal == null ? "NULL" : "NOT NULL"));

        // Add portfolio value for navbar
        if (principal != null) {
            String email = (String) principal.getAttributes().get("email");
            System.out.println("🔍 DEBUG: Email from principal: " + email);

            User user = userRepository.findUserByUsername(email).orElse(null);
            System.out.println("🔍 DEBUG: User from database: " + (user == null ? "NULL" : user.getUsername()));

            if (user != null) {
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

        return "news";
    }
}

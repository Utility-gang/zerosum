package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.CompanyRepository;

import com.utilitygang.zerosum.service.UserContextService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserContextService userContextService;

    @GetMapping({ "/", "/portfolio" })
    public String indexAndPortfolio(Model model, @AuthenticationPrincipal DefaultOidcUser principal,
            HttpServletRequest req) {

        Map<String, Double> holdings = new java.util.HashMap<>();

        // use this to do the couple of changes in logic
        boolean isRoot = req.getRequestURI().equals("/");

        if (isRoot) {
            model.addAttribute("companies", companyRepository.findAll());
        }

        // Add portfolio value for navbar
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            User user = userContextService.getUser(principal);

            if (user != null) {
                holdings = userContextService.getUserHoldings(user);

                // add all the companies that the user has bought
                if (!isRoot) {
                    model.addAttribute("companies", companyRepository.findAllByStockOwner(user));
                }

                model.addAttribute("totalPortfolioValue", userContextService.getUserPortfolioValue(user));
                model.addAttribute("user", user);

            }
        }

        model.addAttribute("holdings", holdings);

        return "index";
    }
}

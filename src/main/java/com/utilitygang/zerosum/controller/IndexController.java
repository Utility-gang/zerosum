package com.utilitygang.zerosum.controller;

import com. utilitygang.zerosum. model.Company;
import com. utilitygang.zerosum. model.User;
import com.utilitygang.zerosum.repository.CompanyRepository;
import com.utilitygang.zerosum.repository.UserRepository;
import com.utilitygang.zerosum.service. PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security. oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util. List;
import java.util. Locale;

@Controller
public class IndexController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioService portfolioService;

    @RequestMapping("/")
    public String index(Model model, @AuthenticationPrincipal DefaultOidcUser principal) {
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);

        // Add portfolio value for navbar
        if (principal != null) {
            String email = (String) principal.getAttributes().get("email");
            User user = userRepository.findUserByUsername(email).orElse(null);

            if (user != null) {
                BigDecimal totalPortfolioValue = portfolioService.calculateTotalPortfolioValue(user);
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
                model.addAttribute("totalPortfolioValue", currencyFormatter.format(totalPortfolioValue));
                model.addAttribute("user", user);
            }
        }

        return "index";
    }
}
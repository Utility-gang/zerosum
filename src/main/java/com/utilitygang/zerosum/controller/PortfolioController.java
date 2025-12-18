package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.*;
import com.utilitygang.zerosum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PortfolioController {
    @Autowired
    CompanyRepository companyRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    StockRepository stockRepo;

    @GetMapping("/portfolio")
    public String portfolioIndex(@AuthenticationPrincipal DefaultOidcUser principal, Model model) {
        User owner = userRepo.findUserByUsername((String) principal.getAttribute("email")).get();
        List<Company> companies = companyRepo.findAllByStockOwner(owner);

        model.addAttribute("companies", companies);

        return ("/index");
    }
}

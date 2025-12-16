package com.utilitygang.zerosum.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class PortfolioController {
    @GetMapping("/portfolio")
    public String portfolioIndex(@AuthenticationPrincipal OidcUser principal, Model model) {
        return "portfolio/index";
    }
}

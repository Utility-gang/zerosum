package com.utilitygang.zerosum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class PortfolioController {
    @GetMapping("/portfolio")
    public String portfolioIndex(Model model) {
        return "portfolio/index";
    }
}

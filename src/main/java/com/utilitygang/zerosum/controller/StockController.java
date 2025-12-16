package com.utilitygang.zerosum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;

@Controller
public class StockController {
    @PostMapping("/stocks/{id}/buy")
    public RedirectView stocksBuy(@PathVariable Long id, @AuthenticationPrincipal OidcUser principal, Model model) {
        return new RedirectView("/portfolio");
    }

    @PostMapping("/stocks/{id}/sell")
    public RedirectView stocksSell(@PathVariable Long id, @AuthenticationPrincipal OidcUser principal, Model model) {
        return new RedirectView("/portfolio");
    }
}

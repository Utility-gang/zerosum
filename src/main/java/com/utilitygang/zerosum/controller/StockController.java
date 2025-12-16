package com.utilitygang.zerosum.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;

@Controller
public class StockController {
    @GetMapping("/stocks")
    public String stocksIndex(@AuthenticationPrincipal OidcUser principal, Model model) {
        return "stocks/index";
    }

    // pass the type of large (for cards in /stocks) or small (for cards in /index)
    // default to large cards
    @GetMapping("/stocks/list/{type}")
    public String stocksList(@PathVariable String type, @AuthenticationPrincipal OidcUser principal, Model model) {
        if (type.equals("small"))
            return "stocks/fragment/bigcards";
        else
            return "stocks/fragment/smallcards";
    }

    @GetMapping("/stocks/{id}")
    public String stocksIndividual(@PathVariable Long id, @AuthenticationPrincipal OidcUser principal, Model model) {
        return "stocks/individual";
    }

    @PostMapping("/stocks/{id}/buy")
    public RedirectView stocksBuy(@PathVariable Long id, @AuthenticationPrincipal OidcUser principal, Model model) {
        return new RedirectView("/stocks" + id);
    }

    @PostMapping("/stocks/{id}/sell")
    public RedirectView stocksSell(@PathVariable Long id, @AuthenticationPrincipal OidcUser principal, Model model) {
        return new RedirectView("/stocks" + id);
    }
}

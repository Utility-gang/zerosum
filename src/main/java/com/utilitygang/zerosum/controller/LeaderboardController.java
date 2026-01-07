package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.UserRepository;
import com.utilitygang.zerosum.service.LeaderboardService;
import com.utilitygang.zerosum.service.UserContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LeaderboardController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserContextService userContextService;

    @Autowired
    LeaderboardService leaderboardService;

    @GetMapping({ "/leaderboard" })
    public String leaderboard(Model model, @AuthenticationPrincipal DefaultOidcUser principal) {
        List<User> topUsers = userRepository.findTop10ByOrderByPortfolioValueAsc();
        List<User> topUsersLossesCalculated = leaderboardService.calculateLosses(topUsers);

        model.addAttribute("topUsers", topUsersLossesCalculated);

        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        // Add portfolio value for navbar
        if (isAuthenticated) {
            User user = userContextService.getUser(principal);

            if (user != null) {
                model.addAttribute("totalPortfolioValue", userContextService.getUserPortfolioValue(user));
                model.addAttribute("user", user);
            }
        }

        return "leaderboard";
    }
}

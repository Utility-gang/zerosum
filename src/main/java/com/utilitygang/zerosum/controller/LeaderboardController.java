package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LeaderboardController {

    @Autowired
    UserRepository userRepository;

    @GetMapping({ "/leaderboard" })
    public String leadboard(Model model) {
        List<User> topUsers = userRepository.findTop10ByOrderByPortfolioValueAsc();

        model.addAttribute("topUsers", topUsers);

        return "leaderboard";
    }
}

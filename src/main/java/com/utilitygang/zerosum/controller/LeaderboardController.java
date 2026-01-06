package com.utilitygang.zerosum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderboardController {

    @GetMapping({ "/leaderboard" })
    public String leadboard() {
        return "leaderboard";
    }
}

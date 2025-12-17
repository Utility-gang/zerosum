package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.Quote;
import com.utilitygang.zerosum.service.FinnhubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class DebugController {

    private final FinnhubService finnhubService;

    public DebugController(FinnhubService finnhubService) {
        this.finnhubService = finnhubService;
    }

    @GetMapping(value = "/quote", produces = "application/json")
    public Quote getCandle(@RequestParam String symbol) {
        return finnhubService.fetchQuote(symbol);
    }
}

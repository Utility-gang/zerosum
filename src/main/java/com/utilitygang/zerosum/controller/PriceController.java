package com.utilitygang.zerosum.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utilitygang.zerosum.data.PriceData;

@RestController
public class PriceController {
    @GetMapping("/stocks/{company_id}/live")
    public List<PriceData.Stock> stockLiveUpdate(
            @PathVariable String company_id,
            @RequestParam(required = false) Long since) {

        List<PriceData.Stock> prices = PriceData.getPrices(company_id);

        if (prices.isEmpty())
            return List.of();

        if (since == null)
            return prices;

        return prices.stream()
                .filter(s -> s.time() > since)
                .collect(Collectors.toList());
    }
}

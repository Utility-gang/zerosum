package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.model.Quote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FinnhubService {

    private final WebClient webClient;

    @Value("${finnhub.api-key}")

    private String apiKey;

    public FinnhubService(WebClient.Builder builder,
                          @Value("${finnhub.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Quote fetchQuote (String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quote")
                        .queryParam("symbol", symbol)
                        .queryParam("token", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Quote.class)
                .block();
    }
}

package com.utilitygang.zerosum.service;
import com.utilitygang.zerosum.client.FinnhubClient;
import com.utilitygang.zerosum.repository.CompanyRepository;
import com.utilitygang.zerosum.model.Quote;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
@Service
public class FinnhubService {
    private final WebClient webClient;
    private final CompanyRepository companyRepository;
    @Value("${finnhub.api-key}")
    private String apiKey;
    @Value("${finnhub.ws-url}")
    private String wsUrl;
    public FinnhubService(
            WebClient.Builder builder,
            CompanyRepository companyRepository,
            @Value("${finnhub.base-url}") String baseUrl
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.companyRepository = companyRepository;
    }
    // REST QUOTE (fallback)
    public Quote fetchQuote(String symbol) {
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
    // WEBSOCKET (primary)
    @PostConstruct
    public void startWebSocket() {
        URI uri = URI.create(wsUrl + "?token=" + apiKey);
        FinnhubClient client = new FinnhubClient(uri, companyRepository);
        client.connect();
    }
}
package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.client.FinnhubClient;
import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.repository.CompanyRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Profile("!test")
public class FinnhubService {

    private final CompanyRepository companyRepository;
    private final String finnhubKey;

    public FinnhubService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        this.finnhubKey = Dotenv.load().get("FINNHUB_API_KEY");
    }

    // when the app starts up, open the websocketConnection
    @PostConstruct
    public void openWebsocketConnection() throws Exception {
        String url = String.format("wss://ws.finnhub.io?token=%s", finnhubKey);
        URI uri = new URI(url);
        FinnhubClient client = new FinnhubClient(uri, companyRepository);
        client.connect();
    }


//    // when the app starts up, send GET requests to the quote endpoint
//    // and update the cached price
//    @PostConstruct
//    public void updateCachedPrices() throws Exception {
//        RestTemplate restTemplate = new RestTemplate();
//
//        for (Company company : companyRepository.findAll()) {
//            String url = String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", company.getSymbol(), finnhubKey);
//
//            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//            if (response != null && response.containsKey("c")) {
//                Double currentPrice = Double.valueOf(response.get("c").toString());
//                company.setCachedPrice(BigDecimal.valueOf(currentPrice));
//                companyRepository.save(company);
//            }
//        }
//    }
}
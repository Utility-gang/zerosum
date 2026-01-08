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

    private static final String DEFAULT_LOGO_URL = "/images/default-stock.jpg";

    private final CompanyRepository companyRepository;
    private final String finnhubKey;
    private final OpenAiService openAiService;

    public FinnhubService(CompanyRepository companyRepository, OpenAiService openAiService) {
        this.companyRepository = companyRepository;
        this.finnhubKey = Dotenv.load().get("FINNHUB_API_KEY");
        this.openAiService = openAiService;
    }

    // when the app starts up, open the websocketConnection
    @PostConstruct
    public void init() throws Exception {
        openWebsocketConnection();
        hydrateMissingLogos();
        hydrateMissingDescriptions();
        updateCachedPrices();
    }

    // when the app starts up, open the websocketConnection

    public void openWebsocketConnection() throws Exception {
        String url = String.format("wss://ws.finnhub.io?token=%s", finnhubKey);
        URI uri = new URI(url);
        FinnhubClient client = new FinnhubClient(uri, companyRepository);
        client.connect();
    }


    // when the app starts up, send GET requests to the quote endpoint
    // and update the cached price

    public void updateCachedPrices() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        for (Company company : companyRepository.findAll()) {
            String url = String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", company.getSymbol(), finnhubKey);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("c")) {
                Double currentPrice = Double.valueOf(response.get("c").toString());
                company.setCachedPrice(BigDecimal.valueOf(currentPrice));
                companyRepository.save(company);
            }
        }
    }

    private void hydrateMissingLogos() {
        RestTemplate restTemplate = new RestTemplate();

        for (Company company : companyRepository.findAll()) {
            if (company.getLogo() != null && !company.getLogo().isBlank()) {
                continue;
            }

            String symbol = company.getSymbol();
            String url = String.format(
                    "https://finnhub.io/api/v1/stock/profile2?symbol=%s&token=%s",
                    symbol, finnhubKey
            );

            try {
                Map<String, Object> profile = restTemplate.getForObject(url, Map.class);

                String logoUrl = null;

                if (profile != null && profile.get("logo") != null) {
                    String candidate = profile.get("logo").toString().trim();
                    if (!candidate.isBlank()) {
                        logoUrl = candidate;
                    }
                }

                if (logoUrl == null) {
                    logoUrl = DEFAULT_LOGO_URL;
                }

                company.setLogo(logoUrl);
                companyRepository.save(company);

                // Finnhub rate-limit safety
                Thread.sleep(250);

            } catch (Exception e) {
                // Always persist a safe default
                company.setLogo(DEFAULT_LOGO_URL);
                companyRepository.save(company);
            }
        }
    }


    // OpenAI description hydration (tone-aware)

    private void hydrateMissingDescriptions() {
        for (Company company : companyRepository.findAll()) {
            if (company.getDescription() != null && !company.getDescription().isBlank()) {
                continue;
            }

            try {
                String description = openAiService.funnyStockDescription(
                        company.getSymbol(),
                        company.getToneTag()      //  tone-driven humor
                );

                if (description != null && !description.isBlank()) {
                    company.setDescription(description.trim());
                    companyRepository.save(company);
                }

                Thread.sleep(700); // OpenAI rate-limit safety

            } catch (Exception ignored) {
                // leave description empty; UI can show fallback
            }
        }
    }



}

package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.client.FinnhubClient;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class FinnhubService {

    @PostConstruct
    public void start() throws Exception {

        Dotenv dotenv = Dotenv.load();
        String finnhubKey = dotenv.get("FINNHUB_API_KEY");

        String url = String.format("wss://ws.finnhub.io?token=%s", finnhubKey);
        URI uri = new URI(url);
        FinnhubClient client = new FinnhubClient(uri);
        client.connect();
    }
}

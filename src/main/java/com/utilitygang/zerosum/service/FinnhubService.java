package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.client.FinnhubClient;
import com.utilitygang.zerosum.repository.CompanyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class FinnhubService {

    private final CompanyRepository companyRepository;

    public FinnhubService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @PostConstruct
    public void start() throws Exception {
        URI uri = new URI("wss://ws.finnhub.io?token=d518fghr01qjia5c0t00d518fghr01qjia5c0t0g");
        FinnhubClient client = new FinnhubClient(uri, companyRepository);
        client.connect();
    }
}
package com.utilitygang.zerosum.service;

import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    //update the cached price in the db for a company
    public void updateCachedPrice(String symbol, Double newPrice) {
        Company company = companyRepository.findBySymbol(symbol);
        company.setCachedPrice(newPrice);
        companyRepository.save(company);
    }
}

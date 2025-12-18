package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping("/")
    public String index(Model model) {
        List<Company> companies = companyRepository.findAll();

        model.addAttribute("companies", companies);

        return ("/index");
    }
}
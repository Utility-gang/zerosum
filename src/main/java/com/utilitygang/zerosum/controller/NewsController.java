package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.service.FinnhubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class NewsController {
    @Autowired
    FinnhubService finnhubService;

    @GetMapping({ "/news" })
    public String news() throws Exception {
        List<Map<String, Object>> news = finnhubService.fetchNews();
        System.out.println(news);
        return "news";
    }
}

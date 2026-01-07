package com.utilitygang.zerosum.controller;

import com.utilitygang.zerosum.model.NewsArticle;
import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.UserRepository;
import com.utilitygang.zerosum.service.FinnhubService;
import com.utilitygang.zerosum.service.NewsService;
import com.utilitygang.zerosum.service.PortfolioService;
import com.utilitygang.zerosum.service.UserContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
public class NewsController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserContextService userContextService;

    @GetMapping({ "/news" })
    public String news(@RequestParam(defaultValue = "0") int page, Model model, @AuthenticationPrincipal DefaultOidcUser principal) throws Exception {
        // articles per page constant
        final int PAGE_SIZE = 10;

        // fetch all cached articles
        List<NewsArticle> allArticles = newsService.getFinancialNews();

        // get the amount of articles we have
        int totalArticles = allArticles.size();

        // calculate the number of pages (casting to an int)
        int totalPages = (int) Math.ceil((double) totalArticles / PAGE_SIZE);

        // calculate the index of the first article on the page
        int startIndex = page * PAGE_SIZE;

        // calculate the index of the last article on the page
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalArticles);

        // reset if theres an invalid starting index
        if (startIndex > totalArticles) {
            page = 0;
            startIndex = 0;
            endIndex = Math.min(PAGE_SIZE, totalArticles);
        }

        // get a subslice of articles representing current page
        List<NewsArticle> pagedArticles = allArticles.subList(startIndex, endIndex);

        model.addAttribute("newsArticles", pagedArticles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // Add portfolio value for navbar
        if (principal != null) {
            User user = userContextService.getUser(principal);

            if (user != null) {
                model.addAttribute("totalPortfolioValue", userContextService.getUserPortfolioValue(user));
                model.addAttribute("user", user);
            }
        }

        return "news";
    }
}

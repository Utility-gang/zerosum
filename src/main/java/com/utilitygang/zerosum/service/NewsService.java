package com.utilitygang.zerosum.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.utilitygang.zerosum.model.NewsArticle;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class NewsService {
    private static final long REFRESH_RATE = 10 * 60 * 500;
    private static final String CACHE_FILE = "news.json";

    private final String finnhubKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private List<NewsArticle> cachedNews;

    public NewsService(ObjectMapper objectMapper) {
        this.finnhubKey = Dotenv.load().get("FINNHUB_API_KEY");
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    // when the application starts, looks for the news.json file to repopulate the
    // news list
    @PostConstruct
    public void deserialize() throws IOException {
        // try to find the file and if it doesn't exist then manually refresh the cache
        File file = new File(CACHE_FILE);
        if (!file.exists()) {
            // might be dangerous? but didnt want to just return an empty list
            refreshNews();
            return;
        }

        // if the file exists, then read its contents into the article list
        try {
            cachedNews = objectMapper.readValue(file, new TypeReference<List<NewsArticle>>() {});
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
    }


    // write the contents of the article list to the file on close
    @PreDestroy
    public void serialize() throws IOException {
        // if the cachedNews list is empty, do nothing
        if (cachedNews == null || cachedNews.isEmpty()) {
            return;
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(CACHE_FILE), cachedNews);
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }

    }

    // this function runs every 5 minutes and refreshes the news articles in the cache
    @Scheduled(fixedRate = REFRESH_RATE)
    public void refreshNews() {
        String url = String.format("https://finnhub.io/api/v1/news?category=general&token=%s", finnhubKey);

        // fetch the news articles from the endpoint and deserialize them into a list of NewsArticle objects
        NewsArticle[] articles = restTemplate.getForObject(url, NewsArticle[].class);
        cachedNews = List.of(articles);
    }

    // getter should return the cached articles
    public List<NewsArticle> getFinancialNews() {
        if (cachedNews == null || cachedNews.isEmpty()) {
            refreshNews();
        }
        return cachedNews;
    }
}

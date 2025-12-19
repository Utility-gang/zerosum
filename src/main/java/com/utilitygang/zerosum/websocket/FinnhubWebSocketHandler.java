
package com.utilitygang.zerosum.websocket;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utilitygang.zerosum.service.MarketPriceService;

public class FinnhubWebSocketHandler {

    private final MarketPriceService marketPriceService;
    private final ObjectMapper mapper = new ObjectMapper();

    public FinnhubWebSocketHandler(MarketPriceService marketPriceService) {
        this.marketPriceService = marketPriceService;
    }

    public void onMessage(String message) throws Exception {
        JsonNode root = mapper.readTree(message);

        if (!root.has("data")) return;

        for (JsonNode node : root.get("data")) {
            String symbol = node.get("s").asText();
            double price = node.get("p").asDouble();
            marketPriceService.updateFromWebSocket(symbol, price);
        }
    }
}

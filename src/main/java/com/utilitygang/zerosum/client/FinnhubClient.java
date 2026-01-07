package com.utilitygang.zerosum.client;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.utilitygang.zerosum.data.PriceData;
import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.repository.CompanyRepository;
import com.utilitygang.zerosum.service.FinnhubService;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

public class FinnhubClient extends WebSocketClient {
    private final CompanyRepository companyRepository;
    private final FinnhubService finnhubService;

    public FinnhubClient(URI serverUri, CompanyRepository companyRepository, FinnhubService finnhubService) {
        super(serverUri);
        this.companyRepository = companyRepository;
        this.finnhubService = finnhubService;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            String query = String.format("{\"type\":\"subscribe\",\"symbol\":\"%s\"}", company.getSymbol());
            send(query);
        }
        System.out.println("new websocket connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
        finnhubService.openWebsocketConnection();
    }

    @Override
    public void onMessage(String response) {
        // get the head of the JSON object
        JSONObject root = new JSONObject(response);

        // check the 'type' property as sometimes it just sends
        // "type":"ping" so ignore those
        String type = root.getString("type");
        if (!Objects.equals(type, "trade")) {
            return;
        }

        // if it is "type":"trade" we get the data array
        // which contains all the trade info
        JSONArray data = root.getJSONArray("data");

        // go through all the trades
        for (int i = 0; i < data.length(); ++i) {
            JSONObject trade = data.getJSONObject(i);

            // put values in price data specifically how we want them in the final graph
            PriceData.setStock(trade.getString("s"), trade.getDouble("p"), trade.getLong("t") / 1000);
        }
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}

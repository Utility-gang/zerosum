package com.utilitygang.zerosum.client;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    private final ExecutorService tradeProcessor = Executors.newSingleThreadExecutor();

    public FinnhubClient(URI serverUri, CompanyRepository companyRepository, FinnhubService finnhubService) {
        super(serverUri);
        this.companyRepository = companyRepository;
        this.finnhubService = finnhubService;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // start working on the queue
        startProcessingTrades();

        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            String query = String.format("{\"type\":\"subscribe\",\"symbol\":\"%s\"}", company.getSymbol());
            send(query);
        }
        System.out.println("new websocket connection opened");
    }

    // process trades from the queue until there are none left
    public void startProcessingTrades() {
        tradeProcessor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String rawJson = messageQueue.take();
                    processTrades(rawJson);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        stopProcessingTrades();

        System.out.println("closed with exit code " + code + " additional info: " + reason);
        finnhubService.openWebsocketConnection();
    }

    // close the process
    public void stopProcessingTrades() {
        tradeProcessor.shutdownNow();
    }

    // instead of processing each trade on message, add it to the queue to be
    //processed
    @Override
    public void onMessage(String response) {
        messageQueue.add(response);
    }

    private void processTrades(String response) {
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

        // get the trade at index 0 because sometimes it sends
        // multiple but we hope (?) that the price doesnt change
        // in that time
        JSONObject trade = data.getJSONObject(0);

        // extract the symbol (ticker) and the price from the trade info
        String symbol = trade.getString("s");
        BigDecimal price = new BigDecimal(trade.getDouble("p"));

        PriceData.setPrice(symbol, price);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}

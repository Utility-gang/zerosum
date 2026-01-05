package com.utilitygang.zerosum.client;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.utilitygang.zerosum.data.PriceData;
import com.utilitygang.zerosum.model.Company;
import com.utilitygang.zerosum.repository.CompanyRepository;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class FinnhubClient extends WebSocketClient {

    @Autowired
    private CompanyRepository companyRepository;

    public FinnhubClient(URI serverUri) {
        super(serverUri);
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

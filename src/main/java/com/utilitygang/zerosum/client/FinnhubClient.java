package com.utilitygang.zerosum.client;

import java.net.URI;
import java.util.Objects;

import com.utilitygang.zerosum.data.PriceData;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

public class FinnhubClient extends WebSocketClient {

    public FinnhubClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public FinnhubClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}");
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
        Double price = trade.getDouble("p");

        PriceData.setPrice(symbol, price);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}
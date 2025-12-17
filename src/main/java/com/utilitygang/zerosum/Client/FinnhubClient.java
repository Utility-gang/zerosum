package com.utilitygang.zerosum.Client;

import java.net.URI;
import java.nio.ByteBuffer;

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
        JSONObject root = new JSONObject(response);
        JSONArray data = root.getJSONArray("data");
        JSONObject trade = data.getJSONObject(0);
        String symbol = trade.getString("s");
        Double price = trade.getDouble("p");

        System.out.println("Symbol: " + symbol + " Price: " + price);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}
package com.utilitygang.zerosum;

import com.utilitygang.zerosum.Controller.FinnhubClient;
import org.java_websocket.client.WebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class ZerosumApplication {

	public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new FinnhubClient(new URI("wss://ws.finnhub.io?token=d518fghr01qjia5c0t00d518fghr01qjia5c0t0g"));
        client.connect();

        SpringApplication.run(ZerosumApplication.class, args);
	}
}

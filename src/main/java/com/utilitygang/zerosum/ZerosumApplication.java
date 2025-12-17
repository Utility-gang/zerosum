package com.utilitygang.zerosum;

import com.utilitygang.zerosum.service.FinnhubService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ZerosumApplication {

	public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(ZerosumApplication.class, args);

        FinnhubService finnhubService = context.getBean(FinnhubService.class);
        finnhubService.start();
	}
}

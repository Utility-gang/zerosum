package com.utilitygang.zerosum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ZerosumApplication {

	public static void main(String[] args) {
        SpringApplication.run(ZerosumApplication.class, args);
	}
}

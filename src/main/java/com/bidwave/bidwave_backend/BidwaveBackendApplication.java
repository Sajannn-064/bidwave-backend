package com.bidwave.bidwave_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BidwaveBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidwaveBackendApplication.class, args);
	}

}
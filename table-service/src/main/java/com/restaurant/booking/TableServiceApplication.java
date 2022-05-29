package com.restaurant.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.restaurant.booking.feign.client")
@SpringBootApplication
public class TableServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TableServiceApplication.class, args);
	}

}

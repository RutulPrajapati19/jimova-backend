package com.enterprise.smartEcommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartEcommerceApplication.class, args);
	}

}

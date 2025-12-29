package com.asm.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

// Async ---> auto gửi hóa đơn qua mail
@EnableAsync
//Vuong edit -> phuc
@EnableFeignClients(basePackages = "com.asm.ecommerce")
@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}

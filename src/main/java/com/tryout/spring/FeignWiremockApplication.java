package com.tryout.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FeignWiremockApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignWiremockApplication.class, args);
	}

}

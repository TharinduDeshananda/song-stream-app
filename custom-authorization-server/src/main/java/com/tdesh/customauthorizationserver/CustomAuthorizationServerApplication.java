package com.tdesh.customauthorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CustomAuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomAuthorizationServerApplication.class, args);
	}

}

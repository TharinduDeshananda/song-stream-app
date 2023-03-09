package com.tdesh.customapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CustomApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomApiGatewayApplication.class, args);
	}

}

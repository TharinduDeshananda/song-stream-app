package com.tdesh.notificationservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

@Configuration
public class GenericConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder getWEbClientBuilder(){
        return WebClient.builder().filter(new ExchangeFilterFunction() {
            @Override
            public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {

                HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String authHeader = httpServletRequest.getHeader("Authorization");
                System.out.println("Auth header received with value: "+authHeader);
                ClientRequest newRequest = ClientRequest.from(request)
                        .header("Authorization",authHeader)
                        .build();
                return next.exchange(newRequest);
            }
        });
    }

}

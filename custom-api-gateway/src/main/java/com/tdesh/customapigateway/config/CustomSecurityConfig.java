package com.tdesh.customapigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .cors().disable()
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(r->r.jwt(j->j.jwkSetUri("http://localhost:9090/oauth2/jwks")))
                .build();
    }

}

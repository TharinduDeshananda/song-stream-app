package com.tdesh.customauthorizationserver.config;

import com.tdesh.customauthorizationserver.dto.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final WebClient.Builder webClientBuilder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading from userName: {}",username);
        Mono<CustomUser> customUserMono = webClientBuilder.baseUrl("http://user-service").build().get().uri("/user/email/{email}", username).retrieve().bodyToMono(CustomUser.class);
        CustomUser user = customUserMono.block();
        System.out.println(user);
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).authorities(user.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList()).build();

        return userDetails;
    }
}

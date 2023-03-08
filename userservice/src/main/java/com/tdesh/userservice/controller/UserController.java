package com.tdesh.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    WebClient.Builder webClientBuilder;

    @GetMapping("/get-shared")
    public ResponseEntity<Mono<String>> getSharedValueFromSong() throws URISyntaxException {

        Mono<String> stringMono = webClientBuilder.build().get().uri(new URI("http://song-service/song/shared")).retrieve().bodyToMono(String.class);

        return ResponseEntity.ok(stringMono);
    }

}

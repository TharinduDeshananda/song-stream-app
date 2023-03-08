package com.tdesh.songservice.controller;


import com.netflix.discovery.shared.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@RestController
@RequestMapping("/song")
public class SongController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${myname}")
    private String myName;

    @Value("${shared}")
    private String sharedValue;

    @Value("${myage}")
    private String myAge;

    @GetMapping("")
    public ResponseEntity<String> getMyName(){

        return ResponseEntity.ok(myName);
    }

    @GetMapping("/shared")
    public ResponseEntity<String> getShared(){

        return ResponseEntity.ok(sharedValue);
    }
    @GetMapping("/age")
    public ResponseEntity<String> getAge(){

        return ResponseEntity.ok(myAge);
    }

    @GetMapping("/discovery-info")
    public ResponseEntity<DiscoveryClient> getDiscoveryClientInfo(){
        List<String> services = discoveryClient.getServices();
        System.out.println(services);
        return ResponseEntity.ok(discoveryClient);
    }


}

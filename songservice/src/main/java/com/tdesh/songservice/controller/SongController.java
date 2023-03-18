package com.tdesh.songservice.controller;

import com.tdesh.songservice.dto.SongSaveRequestDTO;
import com.tdesh.songservice.service.SongService;
import com.tdesh.songservice.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final DiscoveryClient discoveryClient;
    private final SongService songService;
    private final WebClient.Builder webClientBuilder;
    private final S3Util s3Util;

    @GetMapping("/discovery-info")
    public ResponseEntity<DiscoveryClient> getDiscoveryClientInfo(){
        List<String> services = discoveryClient.getServices();
        System.out.println(services);
        return ResponseEntity.ok(discoveryClient);
    }

    @PostMapping("/upload-test-file")
    public ResponseEntity<String> uploadFile(@ModelAttribute SongSaveRequestDTO dto) throws IOException {

        songService.addUserSong(dto);
        return ResponseEntity.ok("Done!!");
    }


}

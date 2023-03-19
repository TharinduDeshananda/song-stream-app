package com.tdesh.songservice.controller;

import com.tdesh.songservice.dto.CommonResponse;
import com.tdesh.songservice.dto.SongSaveRequestDTO;
import com.tdesh.songservice.service.SongService;
import com.tdesh.songservice.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<CommonResponse<String>> uploadFile(@ModelAttribute SongSaveRequestDTO dto) throws IOException {

        songService.addUserSong(dto);
        return ResponseEntity.ok(new CommonResponse<>(0,null,"OPERATION SUCCESS"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getUserUploadsDetails(@PathVariable long userId,@RequestParam(required = false,defaultValue = "0")int page,@RequestParam(required = false,defaultValue = "0")int size){
        return ResponseEntity.ok(songService.getUserUploadDetails(userId,size!=0? PageRequest.of(page,size):Pageable.unpaged()));
    }


}

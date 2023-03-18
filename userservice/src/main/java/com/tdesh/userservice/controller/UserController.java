package com.tdesh.userservice.controller;

import com.tdesh.userservice.dto.request.UserPublisherResponseDTO;
import com.tdesh.userservice.dto.request.UserRequestDTO;
import com.tdesh.userservice.dto.request.UserSubscriberResponseDTO;
import com.tdesh.userservice.dto.response.CommonResponse;
import com.tdesh.userservice.dto.response.UserResponseDTO;
import com.tdesh.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import static com.tdesh.userservice.config.Constants.SUCCESS_CODE;
import static com.tdesh.userservice.config.Constants.SUCCESS_RESPONSE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final WebClient.Builder webClientBuilder;
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserResponseDTO>> getUser(@PathVariable long userId){
        return ResponseEntity.ok(new CommonResponse<>(SUCCESS_CODE,userService.getUser(userId),SUCCESS_RESPONSE));
    }
    @PostMapping()
    public ResponseEntity<CommonResponse<String>> saveUser(@RequestBody UserRequestDTO dto){
        userService.saveUser(dto);
        return ResponseEntity.ok(new CommonResponse<>(SUCCESS_CODE,null,SUCCESS_RESPONSE));
    }

    @GetMapping()
    public ResponseEntity<CommonResponse<Page<UserResponseDTO>>> getUsersFiltered(
            @RequestParam(required = false,defaultValue = "0")long userId,
            @RequestParam(required = false)String userName,
            @RequestParam(required = false)String email,
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "0")int size){

        Page<UserResponseDTO> allUsers = userService.getAllUsers(userId, userName, email,page,size);
        return ResponseEntity.ok(new CommonResponse<>(SUCCESS_CODE,allUsers,SUCCESS_RESPONSE));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email){

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/email/{email}/subscribers")
    public ResponseEntity<UserSubscriberResponseDTO> getUserSubscribersByEmail(@PathVariable String email){

        return ResponseEntity.ok(userService.getUserSubscribers(email));
    }

    @GetMapping("/email/{email}/publishers")
    public ResponseEntity<UserPublisherResponseDTO> getUserPublishersByEmail(@PathVariable String email){

        return ResponseEntity.ok(userService.getUserPublishers(email));
    }

    @PostMapping("/add-subscription")
    public ResponseEntity<CommonResponse<String>> addSubscription(@RequestParam long subId,@RequestParam long pubId){
        userService.addSubscriberToPublisher(subId,pubId);
        return ResponseEntity.ok(new CommonResponse<>(SUCCESS_CODE,null,SUCCESS_RESPONSE));
    }

    @PostMapping("/remove-subscription")
    public ResponseEntity<CommonResponse<String>> removeSubscription(@RequestParam long subId,@RequestParam long pubId){
        userService.removeSubscriberFromPublisher(subId,pubId);
        return ResponseEntity.ok(new CommonResponse<>(SUCCESS_CODE,null,SUCCESS_RESPONSE));
    }
}

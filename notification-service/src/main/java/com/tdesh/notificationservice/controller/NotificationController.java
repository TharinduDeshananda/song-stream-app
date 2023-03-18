package com.tdesh.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class NotificationController {

    @Value("${aws.sqs.queue.name}")
    private String notificationQueueName;

    private final QueueMessagingTemplate queueMessagingTemplate;


    @GetMapping("")
    public ResponseEntity<String> getMain(){
        queueMessagingTemplate.send("songstreamnotificationqueue",MessageBuilder.<String>withPayload("Hello this is second message").build());
        return ResponseEntity.ok("Hello World");
    }




}

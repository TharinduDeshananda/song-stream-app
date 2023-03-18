package com.tdesh.notificationservice.service.impl;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdesh.notificationservice.dto.NotificationRequestDTO;
import com.tdesh.notificationservice.service.NotificationServiceListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class NotificationServiceListenerImpl implements NotificationServiceListener {

    @Value("${aws.sqs.queue.url}")
    private String destination;

    private final QueueMessagingTemplate queueMessagingTemplate;
    private final AmazonSQSAsync amazonSQSAsync;
    private final MailSender mailSender;

    @Override
    @Scheduled(fixedDelay = 1000)
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void listenNotificationMessages() {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            List<SimpleMailMessage> mailList = new ArrayList<>();
            ReceiveMessageRequest messageRequest = new ReceiveMessageRequest(destination)
                    .withMaxNumberOfMessages(2);
            List<Message> messages = amazonSQSAsync.receiveMessage(messageRequest).getMessages();
            messages.forEach(msg->{
                log.info("Message received: {}",msg.getReceiptHandle());
                try {
                    log.info("body is: {}",msg.getBody());
                    NotificationRequestDTO dto = objectMapper.readValue(msg.getBody(),NotificationRequestDTO.class);

                    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                    simpleMailMessage.setFrom("tdeshananda@gmail.com");
                    simpleMailMessage.setTo("tdeshananda@gmail.com");
                    simpleMailMessage.setSubject("User added new song");
                    simpleMailMessage.setText("New Song added by user");

                    mailList.add(simpleMailMessage);

                } catch (JsonProcessingException e) {
                    log.error("Object mapping notification object failed: ",e);
                }
                amazonSQSAsync.deleteMessageAsync(destination,msg.getReceiptHandle());
                mailSender.send(mailList.toArray(new SimpleMailMessage[mailList.size()]));
            });
        } catch (Exception e) {
            log.info("Method listenNotificationMessages failed: {}",e.getMessage());
            throw e;
        }

    }
}

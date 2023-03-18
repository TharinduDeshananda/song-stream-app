package com.tdesh.songservice.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdesh.songservice.dto.*;
import com.tdesh.songservice.entity.SongUpload;
import com.tdesh.songservice.enums.EmailType;
import com.tdesh.songservice.repository.SongUploadRepo;
import com.tdesh.songservice.service.SongService;
import com.tdesh.songservice.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final S3Util s3Util;
    private final ModelMapper modelMapper;
    private final WebClient.Builder webClientBuilder;
    private final SongUploadRepo songUploadRepo;
    private final AmazonSQSAsync amazonSQSAsync;
    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${aws.s3.songfolder}")
    private String songFolder;

    @Value("${aws.sqs.queue.name}")
    private String queueName;
    @Value("{aws.sqs.queue.url}")
    private String queueUrl;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addUserSong(SongSaveRequestDTO dto) throws IOException {
        try {
            log.info("Method addUserSong start ");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String principal = jwt.getClaim("sub");
            String contentType = dto.getFile().getContentType();
            String name = dto.getFile().getName();
            String url = s3Util.uploadInputStreamToS3bucket("largefolder",name,dto.getFile().getInputStream(),contentType);

            CustomUser user = webClientBuilder.build().get().uri("http://user-service/user/email/{email}", principal).retrieve().bodyToMono(CustomUser.class).block();

            UserSubscriberResponseDTO subscriberDto = webClientBuilder.build().get().uri("http://user-service/user/email/{email}/subscribers", principal).retrieve().bodyToMono(UserSubscriberResponseDTO.class).block();

            if(user==null)throw new RuntimeException("User not found");
            SongUpload songUpload = SongUpload.builder()
                            .uploadUrl(url)
                                    .userId(user.getUserId())
                                            .userEmail(user.getEmail())
                                                    .uploadTitle(dto.getUploadTitle())
                                                            .build();

            songUploadRepo.save(songUpload);

            if(subscriberDto==null|| subscriberDto.getSubscribers()==null)throw new RuntimeException("Operation failed, subscribers not found");
            for (UserResponseDTO subscriber : subscriberDto.getSubscribers()) {
                NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                        .email(subscriber.getEmail())
                        .emailType(EmailType.NEW_SONG_ADDED)
                        .attributes(null)
                        .build();

                ObjectMapper objectMapper = new ObjectMapper();
                queueMessagingTemplate.send(queueName,MessageBuilder.<String>withPayload(objectMapper.writeValueAsString(notificationRequestDTO)).build());
            }
            log.info("upload url: {}",url);
            log.info("Method addUserSong userId: {} success",user.getUserId());
        } catch (IOException e) {
            log.info("Method addUserSong failed");
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public List<UserUploadDetails> getUserUploadDetails(long userId, Pageable pageable) {
        try {
            log.info("method getUserUploadDetails userID: {}",userId);
            return songUploadRepo.getSongUploadsOfUser(userId,pageable).stream().map(su->modelMapper.map(su,UserUploadDetails.class)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}

package com.tdesh.songservice.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdesh.songservice.dto.CustomUser;
import com.tdesh.songservice.dto.NotificationRequestDTO;
import com.tdesh.songservice.dto.SongSaveRequestDTO;
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
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

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
            if(user==null)throw new RuntimeException("User not found");
            SongUpload songUpload = SongUpload.builder()
                            .uploadUrl(url)
                                    .userId(user.getUserId())
                                            .userEmail(user.getEmail())
                                                    .uploadTitle(dto.getUploadTitle())
                                                            .build();

            songUploadRepo.save(songUpload);

            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                            .email(user.getEmail())
                                    .emailType(EmailType.NEW_SONG_ADDED)
                                            .attributes(null)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            queueMessagingTemplate.send(queueName,MessageBuilder.<String>withPayload(objectMapper.writeValueAsString(notificationRequestDTO)).build());

            log.info("upload url: {}",url);
            log.info("Method addUserSong userId: {} success",user.getUserId());
        } catch (IOException e) {
            log.info("Method addUserSong failed");
            throw e;
        }
    }
}

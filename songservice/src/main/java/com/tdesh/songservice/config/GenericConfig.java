package com.tdesh.songservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

@Configuration
public class GenericConfig {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    @LoadBalanced
    public WebClient.Builder getWEbClientBuilder(){
        return WebClient.builder().filter(new ExchangeFilterFunction() {
            @Override
            public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {

                HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String authHeader = httpServletRequest.getHeader("Authorization");
                System.out.println("Auth header received with value: "+authHeader);
                ClientRequest newRequest = ClientRequest.from(request)
                        .header("Authorization",authHeader)
                        .build();
                return next.exchange(newRequest);
            }
        });
    }

    @Bean
    public AmazonS3 amazonS3(){

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();


    }

    @Bean
    public ModelMapper modelMapper(){

        return new ModelMapper();
    }

    @Bean(name = "mysqs")
    AmazonSQSAsync getSqsAsync(){
        AmazonSQSAsync sqs = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey)))
                .withRegion(region).build();

        return sqs;
    }

    @Bean
    @DependsOn("mysqs")
    QueueMessagingTemplate getQueueMessagingTemplate(@Qualifier("mysqs") AmazonSQSAsync sqs){
        return new QueueMessagingTemplate(sqs);
    }

}

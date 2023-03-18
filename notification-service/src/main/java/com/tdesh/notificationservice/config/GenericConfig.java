package com.tdesh.notificationservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.sqs.*;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.mail.simplemail.SimpleEmailServiceMailSender;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSender;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

@Configuration
public class GenericConfig {

    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.s3.region}")
    private String sqsRegion;

    @Bean
    ModelMapper modelMapper(){

        return new ModelMapper();
    }

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


    @Bean(name = "mysqs")
    AmazonSQSAsync getSqsAsync(){
        AmazonSQSAsync sqs = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey,awsSecretKey)))
                .withRegion(sqsRegion).build();

        return sqs;
    }

    @Bean
    @DependsOn("mysqs")
    QueueMessagingTemplate getQueueMessagingTemplate(@Qualifier("mysqs") AmazonSQSAsync sqs){
        return new QueueMessagingTemplate(sqs);
    }

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(sqsRegion)
                .build();
    }

    @Bean
    public MailSender mailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
        return new SimpleEmailServiceMailSender(amazonSimpleEmailService);
    }


}

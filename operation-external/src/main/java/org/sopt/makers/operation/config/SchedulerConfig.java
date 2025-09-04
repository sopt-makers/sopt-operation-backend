package org.sopt.makers.operation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.val;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final ValueConfig valueConfig;

    @Bean
    public SchedulerClient schedulerClient() {
        val awsCredentials = AwsBasicCredentials.create(
                valueConfig.getAccessKey(),
                valueConfig.getSecretKey()
        );
        
        return SchedulerClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
} 
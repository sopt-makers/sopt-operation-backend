package org.sopt.makers.operation.client.s3;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import org.sopt.makers.operation.config.ValueConfig;

@Configuration
@RequiredArgsConstructor
public class S3Manager {
    private static final String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
    private static final String AWS_SECRET_ACCESS_KEY = "aws.secretAccessKey";
    private static final String AWS_REGION = "aws.region";
    private final ValueConfig valueConfig;

    @Bean
    public SystemPropertyCredentialsProvider systemPropertyCredentialsProvider() {
        System.setProperty(AWS_ACCESS_KEY_ID, valueConfig.getAccessKey());
        System.setProperty(AWS_SECRET_ACCESS_KEY, valueConfig.getSecretKey());
        System.setProperty(AWS_REGION, valueConfig.getRegion());
        return SystemPropertyCredentialsProvider.create();
    }

    @Bean
    public Region getRegion() {
        return Region.of(valueConfig.getRegion());
    }

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder()
                .region(getRegion())
                .credentialsProvider(systemPropertyCredentialsProvider())
                .build();
    }

    @Bean
    public S3Presigner getS3Presigner() {
        return S3Presigner.builder()
                .region(getRegion())
                .credentialsProvider(systemPropertyCredentialsProvider())
                .build();
    }
}
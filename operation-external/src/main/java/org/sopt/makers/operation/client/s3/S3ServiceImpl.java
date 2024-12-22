package org.sopt.makers.operation.client.s3;

import java.time.Duration;

import lombok.val;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Presigner s3Presigner;
    private static final int SIGNATURE_DURATION = 20;

    @Override
    public String createPutPreSignedUrl(String bucketName, String fileName) {
        val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        val preSignedUrlRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(SIGNATURE_DURATION))
                .putObjectRequest(putObjectRequest)
                .build();
        val url = s3Presigner.presignPutObject(preSignedUrlRequest).url();

        return url.toString();
    }
}
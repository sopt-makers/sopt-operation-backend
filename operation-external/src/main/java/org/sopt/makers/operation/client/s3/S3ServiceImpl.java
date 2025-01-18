package org.sopt.makers.operation.client.s3;

import static org.sopt.makers.operation.code.failure.ExternalFailureCode.NOT_FOUND_S3_RESOURCE;

import java.time.Duration;

import lombok.val;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.exception.*;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private static final int SIGNATURE_DURATION = 20;

    @Override
    public String createPreSignedUrlForPutObject(String bucketName, String fileName) {
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

    @Override
    public String getUrl(String bucketName, String fileName) {
        try {
            return s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(fileName)).toExternalForm();
        } catch(NoSuchKeyException e) {
            throw new ExternalException(NOT_FOUND_S3_RESOURCE);
        }
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        s3Client.deleteObject(b -> b.bucket(bucketName).key(fileName));
    }
}
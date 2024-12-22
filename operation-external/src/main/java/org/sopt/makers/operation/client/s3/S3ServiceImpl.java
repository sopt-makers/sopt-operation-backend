package org.sopt.makers.operation.client.s3;

import java.time.Duration;

import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.config.*;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Presigner s3Presigner;
    private static final int SIGNATURE_DURATION = 20;

    @Override
    public String createPutPreSignedUrl(String bucketName, String fileName) {
        PresignedPutObjectRequest preSignedRequest = s3Presigner
                .presignPutObject(r -> r.signatureDuration(Duration.ofMinutes(SIGNATURE_DURATION))
                        .putObjectRequest(por -> por.bucket(bucketName)
                                .key(fileName)
                                .acl(ObjectCannedACL.PUBLIC_READ)));
        return preSignedRequest.url().toString();
    }
}
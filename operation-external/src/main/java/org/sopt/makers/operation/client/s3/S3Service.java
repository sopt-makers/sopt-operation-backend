package org.sopt.makers.operation.client.s3;

public interface S3Service {
    String createPutPreSignedUrl(String bucketName, String fileName);
}

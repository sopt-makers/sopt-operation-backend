package org.sopt.makers.operation.client.s3;

public interface S3Service {
    String createPreSignedUrlForPutObject(String bucketName, String fileName);

    String getUrl(String bucketName, String fileName);
}

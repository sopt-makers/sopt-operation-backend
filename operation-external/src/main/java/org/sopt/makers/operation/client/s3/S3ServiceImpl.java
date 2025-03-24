package org.sopt.makers.operation.client.s3;

import static org.sopt.makers.operation.code.failure.ExternalFailureCode.NOT_FOUND_S3_RESOURCE;

import java.io.IOException;
import java.time.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.val;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.exception.*;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
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
    private static final Long MAX_FILE_SIZE = 5 * 1024 * 1024L;
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/png");

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


    @Override
    public String uploadImage(String directoryPath, MultipartFile image,String bucketName) throws IOException {
        final String key = directoryPath + generateImageFileName();


        validateExtension(image);
        validateFileSize(image);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(image.getContentType())
                .contentDisposition("inline")
                .build();

        RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
        s3Client.putObject(request, requestBody);
        return key;
    }

    private void validateExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!IMAGE_EXTENSIONS.contains(contentType)) {
            throw new RuntimeException("이미지 확장자는 png만 가능합니다.");
        }
    }

    private String generateImageFileName() {
        return UUID.randomUUID() + ".png";
    }




    private void validateFileSize(MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("이미지 사이즈는 5MB를 넘을 수 없습니다.");
        }
    }

}
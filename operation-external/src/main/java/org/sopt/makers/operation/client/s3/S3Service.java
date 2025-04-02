package org.sopt.makers.operation.client.s3;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String createPreSignedUrlForPutObject(String bucketName, String fileName);

    String getUrl(String bucketName, String fileName);

    void deleteFile(String bucketName, String fileName);

    String uploadImage(String directoryPath, MultipartFile image ,String bucketName) throws IOException;
}

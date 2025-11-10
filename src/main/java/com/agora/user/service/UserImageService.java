package com.agora.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class UserImageService {


    private final S3Client s3Client;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    public UserImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadUserImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        var tempFile = Files.createTempFile("upload-", fileName);
        file.transferTo(tempFile);

        var request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, tempFile);

        return endpoint + "/" + bucketName + "/" + fileName;
    }
}

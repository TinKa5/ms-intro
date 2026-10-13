package com.msintro.resource_service.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Storage {

    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String s3bucketName;


    public String uploadFile(byte[] file) throws IOException {
        //String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = UUID.randomUUID().toString();

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(s3bucketName).key(key).build(),
                RequestBody.fromBytes(file));
        return key;
    }

    public byte[] getFile(String key) {
        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(s3bucketName).key(key).build());
        return responseBytes.asByteArray();
    }

    public void deleteFileList(Collection<String> keys) {
        keys.forEach(this::deleteFile);
    }

    public void deleteFile(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(s3bucketName)
                .key(key)
                .build());
    }
}

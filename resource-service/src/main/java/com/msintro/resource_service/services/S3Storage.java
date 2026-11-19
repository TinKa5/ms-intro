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
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Storage {

    private final S3Client s3Client;

    public String uploadFile(byte[] file, String bucket, String path) throws IOException {
        //String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = path.replaceAll("^/+|/*$", "") + "/" + UUID.randomUUID();


        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucket).key(key).build(),
                RequestBody.fromBytes(file));
        return key;
    }

    public byte[] getFile(String key, String bucket) {
        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(bucket).key(key).build());
        return responseBytes.asByteArray();
    }

    public void deleteFileList(Map<String, String> fileInfo) {
        fileInfo.keySet().forEach(k -> deleteFile(k, fileInfo.get(k)));
    }

    public void deleteFile(String key, String bucket) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    public String moveFile(String oldBucket, String oldKey, String newBucket, String newKey) throws IOException {
        String path = uploadFile(getFile(oldKey, oldBucket), newBucket, newKey);
        deleteFile(oldKey, oldBucket);
        return path;
    }
}

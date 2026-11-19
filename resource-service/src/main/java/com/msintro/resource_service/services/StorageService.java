package com.msintro.resource_service.services;

import com.msintro.resource_service.clients.StorageRestClient;
import com.msintro.resource_service.dto.StorageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${cloud.aws.s3.bucket-type.stage}")
    private String bucketTypeStage;
    @Value("${cloud.aws.s3.bucket-type.permanent}")
    private String bucketTypePermanent;

    private final StorageRestClient storageRestClient;


    public StorageDto getStage(){
        return storageRestClient.getAll().stream()
                .filter(s -> s.storageType().equals(bucketTypeStage))
                .findFirst().orElse(null);
    }

    public StorageDto getPermanent() {
        return storageRestClient.getAll().stream()
                .filter(s -> s.storageType().equals(bucketTypePermanent))
                .findFirst().orElse(null);
    }
}

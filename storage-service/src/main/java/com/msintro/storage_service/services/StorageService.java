package com.msintro.storage_service.services;

import com.msintro.storage_service.dto.StorageDto;
import com.msintro.storage_service.repository.Storage;
import com.msintro.storage_service.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository repository;

    public Long save(StorageDto storageDto) {
        return repository.save(new Storage(null, storageDto.storageType(), storageDto.bucket(), storageDto.path()))
                .getId();
    }

    public List<StorageDto> getAll() {
        return repository.findAll().stream()
                .map(s ->
                        new StorageDto(s.getId(), s.getStorageType(), s.getBucket(), s.getPath()))
                .toList();
    }

    public String delete(String ids) {
        List<Long> existingId = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .filter(repository::existsById)
                .toList();
        repository.deleteAllById(existingId);
        return existingId.stream()
                .map(p -> Long.toString(p))
                .collect(Collectors.joining(","));
    }
}

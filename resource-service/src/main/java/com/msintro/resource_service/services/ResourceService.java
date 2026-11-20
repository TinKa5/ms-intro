package com.msintro.resource_service.services;

import com.msintro.resource_service.clients.SongRestClient;
import com.msintro.resource_service.dto.IdListResponseDto;
import com.msintro.resource_service.dto.IdResponseDto;
import com.msintro.resource_service.dto.StorageDto;
import com.msintro.resource_service.exceptions.*;
import com.msintro.resource_service.repository.AudioRecord;
import com.msintro.resource_service.repository.AudioRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final AudioRecordRepository repository;
    private final SongRestClient client;
    private final S3Storage s3Storage;
    private final KafkaResourceProducer resourceProducer;
    private final StorageService storageService;

    public IdResponseDto save(byte[] audioData) {
        try {
            StorageDto stage = storageService.getStage();
            String key = s3Storage.uploadFile(audioData, stage.bucket(), stage.path());
            AudioRecord record = repository.save(
                    new AudioRecord(null, "audio/mpeg", key, stage.storageType(), stage.bucket()));
            resourceProducer.sendMessage(record.getId());
            return new IdResponseDto(record.getId());
        } catch (Exception ex) {
            throw new S3UploadException();
        }
    }

    public byte[] get(String idStr) {
        Long id = parseId(idStr);
        AudioRecord record = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id));
        return s3Storage.getFile(record.getS3key(), record.getBucket());
    }

    public IdListResponseDto delete(String id) {
        if (id.isBlank()) return new IdListResponseDto(new Long[]{});
        Collection<Long> ids = parseIds(id);
        Collection<Long> result = ids.stream()
                .filter(repository::existsById)
                .toList();
        Map<String, String> keys = result.stream()
                .map(repository::getReferenceById)
                .collect(Collectors.toMap(AudioRecord::getS3key, AudioRecord::getBucket));
        if (!result.isEmpty()) {
            s3Storage.deleteFileList(keys);
            repository.deleteAllById(result);
            String queryString = result.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            client.deleteMetadata(queryString);
        }

        return new IdListResponseDto(result.toArray(Long[]::new));
    }

    @Transactional
    public void updateStatus(Long id){
        try {
            StorageDto permanent = storageService.getPermanent();
            AudioRecord record = repository.getReferenceById(id);
            String key = s3Storage.moveFile(record.getBucket(), record.getS3key(), permanent.bucket(), permanent.path());
            record.setStatus(permanent.storageType());
            record.setBucket(permanent.bucket());
            record.setS3key(key);

        } catch (Exception e) {
            new S3UploadException();
        }
    }

    public Collection<Long> parseIds(String str) {
        if (str != null && str.length() > 200) throw new InvalidLengthIdListException(str.length());
        List<Long> result = new ArrayList<>();

        Arrays.stream(str.split(","))
                .forEach(id -> {
                    try {
                        result.add(Long.valueOf(id));
                    } catch (NumberFormatException e) {
                        throw new InvalidIdListException(id);
                    }
                });
        return result;
    }

    private Long parseId(String str) {
        try {
            Long result = Long.valueOf(str);
            if (result > 0) return result;
        } catch (NumberFormatException ex) {

        }
        throw new InvalidIdException(str);
    }

}

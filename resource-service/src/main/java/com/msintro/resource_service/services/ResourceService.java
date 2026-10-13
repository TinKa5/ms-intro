package com.msintro.resource_service.services;

import com.msintro.resource_service.clients.SongRestClient;
import com.msintro.resource_service.dto.IdListResponseDto;
import com.msintro.resource_service.dto.IdResponseDto;
import com.msintro.resource_service.exceptions.*;
import com.msintro.resource_service.repository.AudioRecord;
import com.msintro.resource_service.repository.AudioRecordRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Data
public class ResourceService {

    private final AudioRecordRepository repository;
    private final SongRestClient client;
    private final S3Storage s3Storage;

    public IdResponseDto save(byte[] audioData) {
        try {
            String key = s3Storage.uploadFile(audioData);
            AudioRecord record = repository.save(new AudioRecord(null, "audio/mpeg", key));
            return new IdResponseDto(record.getId());
        } catch (Exception ex) {
            throw new S3UploadException();
        }
    }

    public byte[] get(String idStr) {
        Long id = parseId(idStr);
        AudioRecord record = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id));
        return s3Storage.getFile(record.getS3key());
    }

    public IdListResponseDto delete(String id) {
        if (id.isBlank()) return new IdListResponseDto(new Long[]{});
        Collection<Long> ids = parseIds(id);
        Collection<Long> result = ids.stream()
                .filter(repository::existsById)
                .toList();
        Collection<String> keys = result.stream()
                .map(repository::getReferenceById)
                .map(AudioRecord::getS3key)
                .toList();
        s3Storage.deleteFileList(keys);
        repository.deleteAllById(result);

        return new IdListResponseDto(result.toArray(Long[]::new));
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

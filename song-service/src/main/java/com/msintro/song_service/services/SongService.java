package com.msintro.song_service.services;

import com.msintro.song_service.dto.IdDto;
import com.msintro.song_service.dto.IdListResponseDto;
import com.msintro.song_service.dto.MetadataDto;
import com.msintro.song_service.exceptions.*;
import com.msintro.song_service.mappers.MetadataMapper;
import com.msintro.song_service.repository.Metadata;
import com.msintro.song_service.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository repository;
    private final MetadataMapper mapper;

    public IdDto save(MetadataDto dto) {
        Metadata entity = mapper.toEntity(dto);
        if (repository.findById(entity.getId()).isPresent()) {
            throw new SongAlreadyExistException(dto.getId());
        }
        return new IdDto(repository.save(entity).getId());
    }

    public MetadataDto get(String idStr) {
        Long id = parseId(idStr);
        Metadata record = repository.findById(id).orElseThrow(
                () -> new SongNotFoundException(id));
        return mapper.toDto(record);
    }

    public IdListResponseDto delete(String id) {
        if (id.isBlank()) return new IdListResponseDto(new Long[]{});
        Collection<Long> ids = parseIds(id);
        Collection<Long> result = ids.stream()
                .filter(repository::existsById)
                .toList();
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

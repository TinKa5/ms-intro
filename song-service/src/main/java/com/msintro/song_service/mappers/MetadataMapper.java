package com.msintro.song_service.mappers;

import com.msintro.song_service.dto.MetadataDto;
import com.msintro.song_service.repository.Metadata;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class MetadataMapper {

    public Metadata toEntity(MetadataDto dto) {
        return new Metadata(
                dto.getId(),
                dto.getName(),
                dto.getArtist(),
                dto.getAlbum(),
                dto.getDuration(),
                Integer.valueOf(dto.getYear())
        );
    }

    public MetadataDto toDto(Metadata entity) {
        return new MetadataDto(
                entity.getId(),
                entity.getName(),
                entity.getArtist(),
                entity.getAlbum(),
                entity.getDuration(),
                entity.getYear().toString()
        );
    }


}

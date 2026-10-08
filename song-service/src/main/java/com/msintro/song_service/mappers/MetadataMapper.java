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
                parseDuration(dto.getDuration()),
                Integer.valueOf(dto.getYear())
        );
    }

    public MetadataDto toDto(Metadata entity) {
        return new MetadataDto(
                entity.getId(),
                entity.getName(),
                entity.getArtist(),
                entity.getAlbum(),
                parseDuration(entity.getDuration()),
                entity.getYear().toString()
        );
    }

    private Duration parseDuration(String str) {
        String[] parts = str.split(":");

        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);

        return Duration.ofMinutes(minutes)
                .plusSeconds(seconds);
    }

    private String parseDuration(Duration duration) {
        long min = duration.toMinutes();
        long sec = duration.minusMinutes(min).getSeconds();
        return String.format("%02d:%02d", min, sec);
    }

}

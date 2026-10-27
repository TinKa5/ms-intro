package com.msintro.song_service.services;

import com.msintro.song_service.SongServiceTestcontainersConfiguration;
import com.msintro.song_service.dto.MetadataDto;
import com.msintro.song_service.mappers.MetadataMapper;
import com.msintro.song_service.repository.Metadata;
import com.msintro.song_service.repository.SongRepository;
import org.hibernate.annotations.Audited;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({SongServiceTestcontainersConfiguration.class, SongService.class, MetadataMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SongServiceTest {

    @Autowired
    private SongService service;

    @Test
    void shouldSaveMetadataToDB(){
        MetadataDto metadata = new MetadataDto(1L,"Name", "Artist", "Album", "Duration", "1990");

        assertEquals(1L, service.save(metadata).getId());

    }

}
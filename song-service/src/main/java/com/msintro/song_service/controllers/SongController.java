package com.msintro.song_service.controllers;

import com.msintro.song_service.dto.IdDto;
import com.msintro.song_service.dto.IdListResponseDto;
import com.msintro.song_service.dto.MetadataDto;
import com.msintro.song_service.services.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService service;

    @PostMapping
    public ResponseEntity<IdDto> save(@RequestBody @Valid MetadataDto metadataDto) {

        return ResponseEntity.ok(service.save(metadataDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetadataDto> get(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @DeleteMapping
    public ResponseEntity<IdListResponseDto> delete(@RequestParam String id) {
        return ResponseEntity.ok(service.delete(id));
    }


}

package com.msintro.resource_service.controllers;

import com.msintro.resource_service.dto.IdListResponseDto;
import com.msintro.resource_service.dto.IdResponseDto;
import com.msintro.resource_service.services.ResourceService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/resources")
@Data
public class ResourceController {

    private final ResourceService service;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<IdResponseDto> save(@RequestBody byte[] audioData) {
        return ResponseEntity.ok(service.save(audioData));
    }

    @GetMapping(value = "/{id}",
            produces = "audio/mpeg")
    public ResponseEntity<byte[]> get(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @DeleteMapping
    public ResponseEntity<IdListResponseDto> delete(@RequestParam String id) {
        return ResponseEntity.ok(service.delete(id));
    }

}

package com.msintro.storage_service.controllers;

import com.msintro.storage_service.dto.StorageDto;
import com.msintro.storage_service.services.StorageService;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/storages")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Map<String, Long>> save(@RequestBody StorageDto storageDto) {
        return ResponseEntity.ok(Map.of("id", storageService.save(storageDto)));
    }


    @GetMapping
    public ResponseEntity<List<StorageDto>> get() {
        return ResponseEntity.ok(storageService.getAll());
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> delete(@QueryParam("id") String ids) {
        return ResponseEntity.ok(Map.of("ids", storageService.delete(ids)));
    }

}


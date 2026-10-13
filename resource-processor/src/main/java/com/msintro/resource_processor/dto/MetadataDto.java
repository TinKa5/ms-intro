package com.msintro.resource_processor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MetadataDto {
    private Long id;

    private String name;

    private String artist;

    private String album;

    private String duration;

    private String year;
}

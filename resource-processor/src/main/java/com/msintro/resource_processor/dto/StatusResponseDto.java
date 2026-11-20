package com.msintro.resource_processor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StatusResponseDto {
    private Status status;
    private Long id;
}

package com.msintro.resource_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdListResponseDto {
    private Long[] ids;
}

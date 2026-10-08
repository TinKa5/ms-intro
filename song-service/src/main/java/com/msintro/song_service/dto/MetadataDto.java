package com.msintro.song_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetadataDto {
    @NotNull(message = "ID is required")
    @Positive(message = "ID must be positive")
    private Long id;

    @NotNull(message = "Song name is required")
    @Size(min = 1, max = 100, message = "Song name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Artist name is required")
    @Size(min = 1, max = 100, message = "Artist name must be between 1 and 100 characters")
    private String artist;

    @NotNull(message = "Album name is required")
    @Size(min = 1, max = 100, message = "Album name must be between 1 and 100 characters")
    private String album;

    @NotNull(message = "Duration is required")
    @Pattern(regexp = "^[0-5]\\d:[0-5]\\d$", message = "Duration must be in mm:ss format with leading zeros")
    private String duration;

    @NotNull(message = "Year is required")
    @Pattern(
            regexp = "^(19|20)\\d{2}$",
            message = "Year must be between 1900 and 2099"
    )
    private String year;
}

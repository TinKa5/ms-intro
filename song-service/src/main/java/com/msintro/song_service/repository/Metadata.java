package com.msintro.song_service.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
    @Id
    private Long id;

    private String name; //1-100 characters text.

    private String artist; // 1-100 characters text.

    private String album; // 1-100 characters text.

    private String duration; // Format mm:ss, with leading zeros.

    private Integer year; // YYYY format b
}

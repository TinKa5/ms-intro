package com.msintro.song_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Metadata, Long> {
}

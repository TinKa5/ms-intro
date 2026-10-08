package com.msintro.song_service.exceptions;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(Long id) {
        super("Song metadata for ID=" + id + " not found");
    }
}

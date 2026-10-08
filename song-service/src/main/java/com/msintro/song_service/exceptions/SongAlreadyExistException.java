package com.msintro.song_service.exceptions;

public class SongAlreadyExistException extends RuntimeException {

    public SongAlreadyExistException(Long id) {
        super("Metadata for resource ID=" + id + " already exists");
    }
}

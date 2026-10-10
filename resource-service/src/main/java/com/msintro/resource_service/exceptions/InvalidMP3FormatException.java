package com.msintro.resource_service.exceptions;

public class InvalidMP3FormatException extends RuntimeException {
    public InvalidMP3FormatException() {
        super("Invalid MP3 format.");
    }
}

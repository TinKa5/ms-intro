package com.msintro.resource_service.exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException(String id) {
        super("Invalid value '" + id + "' for ID. Must be a positive integer");
    }
}

package com.msintro.resource_service.exceptions;

public class InvalidIdListException extends RuntimeException {
    public InvalidIdListException(String id) {
        super("Invalid ID format: '" + id + "'. Only positive integers are allowed");
    }
}

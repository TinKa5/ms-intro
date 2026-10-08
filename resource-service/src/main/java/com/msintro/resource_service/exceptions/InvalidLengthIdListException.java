package com.msintro.resource_service.exceptions;

public class InvalidLengthIdListException extends RuntimeException {
    public InvalidLengthIdListException(int length) {
        super("CSV string is too long: received " + length + " characters, maximum allowed is 200");
    }
}

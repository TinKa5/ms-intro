package com.msintro.resource_service.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("Resource with ID=" + id + " not found");
    }
}

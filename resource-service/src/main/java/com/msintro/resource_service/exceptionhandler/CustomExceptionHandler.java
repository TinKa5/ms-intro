package com.msintro.resource_service.exceptionhandler;

import com.msintro.resource_service.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResourceType(
            HttpMediaTypeNotSupportedException ex) {

        ErrorResponse response = new ErrorResponse(
                "Invalid file format: " + ex.getContentType() + ". Only MP3 files are allowed",
                "400"
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(InvalidMP3FormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResourceType(
            InvalidMP3FormatException ex) {

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(), "400"
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, String> details = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(violation ->
                details.put(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )
        );

        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        "Validation error",
                        details,
                        "400"
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        String message = "Invalid value '" + ex.getValue() + "' for '" + ex.getName() + "'. Must be a positive integer";
        return ResponseEntity.badRequest().body(
                new ErrorResponse(message, "400")
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(404).body(
                new ErrorResponse(ex.getMessage(), "404")
        );
    }

    @ExceptionHandler({InvalidIdException.class, InvalidIdListException.class, InvalidLengthIdListException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            RuntimeException ex) {

        return ResponseEntity.badRequest().body(
                new ErrorResponse(ex.getMessage(), "400")
        );
    }
}

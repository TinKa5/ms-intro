package com.msintro.song_service.exceptionhandlers;

import com.msintro.song_service.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(SongAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleSongAlreadyExistException(
            SongAlreadyExistException ex
    ) {

        return ResponseEntity.status(409).body(new ErrorResponse(
                ex.getMessage(),
                "409"
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            MethodArgumentNotValidException ex) {

        Map<String, String> details = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                details.put(
                        error.getField().toString(),
                        error.getDefaultMessage()
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

    @ExceptionHandler({InvalidIdException.class, InvalidIdListException.class, InvalidLengthIdListException.class})
    public ResponseEntity<ErrorResponse> handleInvalidId(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        ex.getMessage(),
                        "400"
                )
        );
    }

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSongNotFound(SongNotFoundException ex) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(
                        ex.getMessage(),
                        "404"
                )
        );
    }
}

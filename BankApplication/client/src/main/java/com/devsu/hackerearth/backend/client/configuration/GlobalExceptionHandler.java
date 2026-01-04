package com.devsu.hackerearth.backend.client.configuration;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "message", "Ocurrió un error inesperado"
            )
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customHandler(CustomException ce) {
        return ResponseEntity.status(ce.getStatus()).body(
            Map.of(
                "timestamp", Instant.now(),
                "status", ce.getStatus().value(),
                "message", ce.getMessage()
            )
        );
    }
}

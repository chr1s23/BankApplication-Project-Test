package com.devsu.hackerearth.backend.account.configuration;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> webClientHandler(WebClientResponseException wce) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            CustomException response = mapper.readValue(wce.getResponseBodyAsString(), CustomException.class);
            return ResponseEntity.status(wce.getStatusCode()).body(response);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(wce.getStatusCode()).body(wce.getResponseBodyAsString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "timestamp", Instant.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "message", "Ocurrió un error inesperado"));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customHandler(CustomException ce) {
        return ResponseEntity.status(ce.getStatus()).body(
                Map.of(
                        "timestamp", Instant.now(),
                        "status", ce.getStatus().value(),
                        "message", ce.getMessage()));
    }
}
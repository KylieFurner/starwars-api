package com.kyliescode.starwars_api_challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the Star Wars API Challenge application.
 * Handles specific exceptions and provides appropriate HTTP responses.
 */
@ControllerAdvice
public class GloabalExceptionHandler {

    /**
     * Handles ResourceNotFound exceptions.
     * Returns a NOT_FOUND response (404) with the exception message.
     *
     * @param ex The ResourceNotFound exception
     * @return ResponseEntity with the exception message and NOT_FOUND status
     */
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles general Exception instances.
     * Returns an INTERNAL_SERVER_ERROR response (500) with the exception message.
     *
     * @param ex The Exception instance
     * @return ResponseEntity with the exception message and INTERNAL_SERVER_ERROR
     *         status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

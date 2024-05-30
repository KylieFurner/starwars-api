package com.kyliescode.starwars_api_challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for resource not found scenarios.
 * Returns a NOT_FOUND status (404) when thrown.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {

    /**
     * Constructs a new ResourceNotFound exception with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public ResourceNotFound(String message) {
        super(message);
    }
}

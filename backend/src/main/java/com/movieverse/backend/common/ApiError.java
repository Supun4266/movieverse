package com.movieverse.backend.common;

import java.time.Instant;

/**
 * The JSON body we send back when something goes wrong.
 * Example: {"status":404,"message":"Movie 99 not found","timestamp":"..."}
 */
public record ApiError(int status, String message, Instant timestamp) {

    public static ApiError of(int status, String message) {
        return new ApiError(status, message, Instant.now());
    }
}

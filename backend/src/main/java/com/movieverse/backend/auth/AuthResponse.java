package com.movieverse.backend.auth;

/**
 * What we send back after a successful login: the JWT token and the username.
 */
public record AuthResponse(String token, String username) {
}

package com.movieverse.backend.watch;

/**
 * The message we send to Kafka when someone presses Play.
 * Example JSON: {"movieId":1,"movieTitle":"Inception","username":"supun","watchedAt":"2026-10-19T10:00:00Z"}
 */
public record WatchEvent(Long movieId, String movieTitle, String username, String watchedAt) {
}

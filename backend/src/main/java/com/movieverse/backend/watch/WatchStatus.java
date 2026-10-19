package com.movieverse.backend.watch;

import com.movieverse.backend.movie.Movie;

/**
 * A movie plus how much of it the user has watched (0-100).
 */
public record WatchStatus(Movie movie, int progressPercent) {
}

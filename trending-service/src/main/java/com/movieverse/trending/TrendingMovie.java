package com.movieverse.trending;

/**
 * One entry in the Trending Now list.
 */
public record TrendingMovie(Long movieId, String title, long views) {
}

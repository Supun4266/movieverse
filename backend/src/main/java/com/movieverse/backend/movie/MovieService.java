package com.movieverse.backend.movie;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Business logic for movies. Controllers call this; this calls the repository.
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findMovies(String genre, String search) {
        if (search != null && !search.isBlank()) {
            return movieRepository.findByTitleContainingIgnoreCaseOrderByRatingDesc(search.trim());
        }
        if (genre != null && !genre.isBlank()) {
            return movieRepository.findByGenreIgnoreCaseOrderByRatingDesc(genre.trim());
        }
        return movieRepository.findAllByOrderByRatingDesc();
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie " + id + " not found"));
    }

    public List<String> getGenres() {
        return movieRepository.findAll().stream()
                .map(Movie::getGenre)
                .distinct()
                .sorted()
                .toList();
    }
}

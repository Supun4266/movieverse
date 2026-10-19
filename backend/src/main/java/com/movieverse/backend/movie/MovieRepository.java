package com.movieverse.backend.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Talks to the database for us. Spring Data writes the SQL from the method names.
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenreIgnoreCaseOrderByRatingDesc(String genre);

    List<Movie> findByTitleContainingIgnoreCaseOrderByRatingDesc(String text);

    List<Movie> findAllByOrderByRatingDesc();
}

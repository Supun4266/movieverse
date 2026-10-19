package com.movieverse.backend.movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A movie in the catalog. @Entity means JPA saves it as a row in the "movies" table.
 */
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private String genre;
    private int releaseYear;
    private double rating;
    private int durationMinutes;
    private String trailerUrl;

    // JPA needs an empty constructor
    protected Movie() {
    }

    public Movie(String title, String description, String genre, int releaseYear,
                 double rating, int durationMinutes, String trailerUrl) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.durationMinutes = durationMinutes;
        this.trailerUrl = trailerUrl;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }
}

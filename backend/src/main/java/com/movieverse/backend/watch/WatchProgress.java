package com.movieverse.backend.watch;

import java.time.Instant;

import com.movieverse.backend.auth.AppUser;
import com.movieverse.backend.movie.Movie;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * How far a user got in a movie. Used for the "Continue Watching" row.
 */
@Entity
@Table(name = "watch_progress",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "movie_id"}))
public class WatchProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AppUser user;

    // EAGER: we always need the movie details when we show this progress
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Movie movie;

    private int progressPercent;

    private Instant updatedAt;

    protected WatchProgress() {
    }

    public WatchProgress(AppUser user, Movie movie) {
        this.user = user;
        this.movie = movie;
        this.progressPercent = 0;
        this.updatedAt = Instant.now();
    }

    public void updateProgress(int percent) {
        this.progressPercent = Math.max(0, Math.min(100, percent));
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

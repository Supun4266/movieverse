package com.movieverse.backend.watch;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movieverse.backend.auth.AppUser;
import com.movieverse.backend.auth.AuthService;
import com.movieverse.backend.movie.Movie;
import com.movieverse.backend.movie.MovieService;

@Service
public class WatchService {

    // Above this, we treat the movie as finished and hide it from "Continue Watching"
    static final int FINISHED_PERCENT = 95;

    private final WatchProgressRepository progressRepository;
    private final AuthService authService;
    private final MovieService movieService;
    private final WatchEventPublisher eventPublisher;

    public WatchService(WatchProgressRepository progressRepository, AuthService authService,
                        MovieService movieService, WatchEventPublisher eventPublisher) {
        this.progressRepository = progressRepository;
        this.authService = authService;
        this.movieService = movieService;
        this.eventPublisher = eventPublisher;
    }

    /** Called when the user presses Play. Saves progress and sends a Kafka event. */
    @Transactional
    public WatchStatus startWatching(String username, Long movieId) {
        Movie movie = movieService.getMovie(movieId);
        WatchProgress progress = findOrCreate(username, movie);

        // Watching a finished movie again starts from the beginning
        if (progress.getProgressPercent() >= FINISHED_PERCENT) {
            progress.updateProgress(0);
        } else {
            progress.updateProgress(progress.getProgressPercent());
        }
        progressRepository.save(progress);

        eventPublisher.publish(new WatchEvent(movie.getId(), movie.getTitle(), username, Instant.now().toString()));
        return new WatchStatus(movie, progress.getProgressPercent());
    }

    @Transactional
    public WatchStatus updateProgress(String username, Long movieId, int percent) {
        Movie movie = movieService.getMovie(movieId);
        WatchProgress progress = findOrCreate(username, movie);
        progress.updateProgress(percent);
        progressRepository.save(progress);
        return new WatchStatus(movie, progress.getProgressPercent());
    }

    @Transactional(readOnly = true)
    public WatchStatus getStatus(String username, Long movieId) {
        Movie movie = movieService.getMovie(movieId);
        int percent = progressRepository.findByUserUsernameAndMovieId(username, movieId)
                .map(WatchProgress::getProgressPercent)
                .orElse(0);
        return new WatchStatus(movie, percent);
    }

    /** Movies the user started but did not finish, newest first. */
    @Transactional(readOnly = true)
    public List<WatchStatus> continueWatching(String username) {
        return progressRepository.findByUserUsernameOrderByUpdatedAtDesc(username).stream()
                .filter(p -> p.getProgressPercent() > 0 && p.getProgressPercent() < FINISHED_PERCENT)
                .map(p -> new WatchStatus(p.getMovie(), p.getProgressPercent()))
                .toList();
    }

    private WatchProgress findOrCreate(String username, Movie movie) {
        return progressRepository.findByUserUsernameAndMovieId(username, movie.getId())
                .orElseGet(() -> {
                    AppUser user = authService.getUser(username);
                    return new WatchProgress(user, movie);
                });
    }
}

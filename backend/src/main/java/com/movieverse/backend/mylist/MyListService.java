package com.movieverse.backend.mylist;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movieverse.backend.auth.AppUser;
import com.movieverse.backend.auth.AuthService;
import com.movieverse.backend.movie.Movie;
import com.movieverse.backend.movie.MovieService;

/**
 * "My List": movies a user saved to watch later.
 */
@Service
public class MyListService {

    private final AuthService authService;
    private final MovieService movieService;

    public MyListService(AuthService authService, MovieService movieService) {
        this.authService = authService;
        this.movieService = movieService;
    }

    @Transactional(readOnly = true)
    public List<Movie> getMyList(String username) {
        AppUser user = authService.getUser(username);
        return user.getMyList().stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .toList();
    }

    @Transactional
    public List<Movie> addMovie(String username, Long movieId) {
        AppUser user = authService.getUser(username);
        user.getMyList().add(movieService.getMovie(movieId));
        return getMyList(username);
    }

    @Transactional
    public List<Movie> removeMovie(String username, Long movieId) {
        AppUser user = authService.getUser(username);
        user.getMyList().removeIf(movie -> movie.getId().equals(movieId));
        return getMyList(username);
    }
}

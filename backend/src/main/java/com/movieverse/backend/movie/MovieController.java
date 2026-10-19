package com.movieverse.backend.movie;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public movie endpoints (no login needed).
 *
 * GET /api/movies                -> all movies
 * GET /api/movies?genre=Sci-Fi   -> movies in one genre
 * GET /api/movies?search=dark    -> search by title
 * GET /api/movies/{id}           -> one movie
 * GET /api/movies/genres         -> list of genres
 */
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getMovies(@RequestParam(required = false) String genre,
                                 @RequestParam(required = false) String search) {
        return movieService.findMovies(genre, search);
    }

    @GetMapping("/genres")
    public List<String> getGenres() {
        return movieService.getGenres();
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }
}

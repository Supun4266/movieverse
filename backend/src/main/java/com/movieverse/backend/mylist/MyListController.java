package com.movieverse.backend.mylist;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieverse.backend.movie.Movie;

/**
 * Login required. The username comes from the JWT token ("subject").
 *
 * GET    /api/my-list            -> my saved movies
 * POST   /api/my-list/{movieId}  -> save a movie
 * DELETE /api/my-list/{movieId}  -> remove a movie
 */
@RestController
@RequestMapping("/api/my-list")
public class MyListController {

    private final MyListService myListService;

    public MyListController(MyListService myListService) {
        this.myListService = myListService;
    }

    @GetMapping
    public List<Movie> getMyList(@AuthenticationPrincipal Jwt jwt) {
        return myListService.getMyList(jwt.getSubject());
    }

    @PostMapping("/{movieId}")
    public List<Movie> addMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        return myListService.addMovie(jwt.getSubject(), movieId);
    }

    @DeleteMapping("/{movieId}")
    public List<Movie> removeMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        return myListService.removeMovie(jwt.getSubject(), movieId);
    }
}

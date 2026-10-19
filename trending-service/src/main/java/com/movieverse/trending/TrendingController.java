package com.movieverse.trending;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * GET /api/trending          -> top 10 most played movies
 * GET /api/trending?limit=5  -> top 5
 */
@RestController
@RequestMapping("/api/trending")
@CrossOrigin(origins = "${movieverse.cors.allowed-origins}")
public class TrendingController {

    private final TrendingStore trendingStore;

    public TrendingController(TrendingStore trendingStore) {
        this.trendingStore = trendingStore;
    }

    @GetMapping
    public List<TrendingMovie> getTrending(@RequestParam(defaultValue = "10") int limit) {
        return trendingStore.top(Math.max(1, Math.min(limit, 50)));
    }
}

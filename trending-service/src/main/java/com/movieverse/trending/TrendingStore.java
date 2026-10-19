package com.movieverse.trending;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Keeps a view count per movie in memory.
 *
 * ConcurrentHashMap is used because Kafka messages and web requests arrive on
 * different threads at the same time.
 */
@Component
public class TrendingStore {

    private final Map<Long, TrendingMovie> views = new ConcurrentHashMap<>();

    public void recordView(Long movieId, String title) {
        views.merge(movieId, new TrendingMovie(movieId, title, 1),
                (old, ignored) -> new TrendingMovie(movieId, title, old.views() + 1));
    }

    public List<TrendingMovie> top(int limit) {
        return views.values().stream()
                .sorted(Comparator.comparingLong(TrendingMovie::views).reversed()
                        .thenComparing(TrendingMovie::title))
                .limit(limit)
                .toList();
    }
}

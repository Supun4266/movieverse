package com.movieverse.trending;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class TrendingStoreTests {

    @Test
    void mostWatchedMovieComesFirst() {
        TrendingStore store = new TrendingStore();
        store.recordView(1L, "Inception");
        store.recordView(2L, "Coco");
        store.recordView(2L, "Coco");
        store.recordView(3L, "Parasite");
        store.recordView(2L, "Coco");
        store.recordView(1L, "Inception");

        List<TrendingMovie> top = store.top(2);

        assertEquals(2, top.size());
        assertEquals("Coco", top.get(0).title());
        assertEquals(3, top.get(0).views());
        assertEquals("Inception", top.get(1).title());
        assertEquals(2, top.get(1).views());
    }
}

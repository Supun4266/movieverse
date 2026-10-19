package com.movieverse.trending;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Listens to the "movie-watched" topic. Every time the backend sends an event,
 * Spring calls onWatchEvent with the message.
 */
@Component
public class WatchEventListener {

    private static final Logger log = LoggerFactory.getLogger(WatchEventListener.class);

    private final TrendingStore trendingStore;
    private final ObjectMapper objectMapper;

    public WatchEventListener(TrendingStore trendingStore, ObjectMapper objectMapper) {
        this.trendingStore = trendingStore;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${movieverse.kafka.topic}")
    public void onWatchEvent(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            long movieId = event.get("movieId").asLong();
            String title = event.get("movieTitle").asText();
            trendingStore.recordView(movieId, title);
            log.info("Someone watched '{}'", title);
        } catch (Exception e) {
            // A bad message should not stop the listener
            log.warn("Skipping a message we could not read: {}", message);
        }
    }
}

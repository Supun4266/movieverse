package com.movieverse.backend.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Rings the Kafka "bell": sends a WatchEvent to the movie-watched topic.
 * Other services (like trending-service) listen to this topic. The main app does not
 * need to know who is listening, which keeps the services loosely connected.
 */
@Component
public class WatchEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(WatchEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;
    private final boolean enabled;

    public WatchEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                               ObjectMapper objectMapper,
                               @Value("${movieverse.kafka.topic}") String topic,
                               @Value("${movieverse.kafka.enabled}") boolean enabled) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
        this.enabled = enabled;
    }

    public void publish(WatchEvent event) {
        if (!enabled) {
            log.debug("Kafka is turned off, not sending {}", event);
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(event);
            // The key (movie id) makes sure all events for one movie go to the same partition
            kafkaTemplate.send(topic, String.valueOf(event.movieId()), json)
                    .whenComplete((result, error) -> {
                        if (error != null) {
                            log.warn("Could not send watch event to Kafka (is Kafka running?): {}", error.getMessage());
                        } else {
                            log.info("Sent watch event for '{}' to topic {}", event.movieTitle(), topic);
                        }
                    });
        } catch (Exception e) {
            // Watching a movie should still work even if Kafka is down
            log.warn("Could not send watch event to Kafka (is Kafka running?): {}", e.getMessage());
        }
    }
}

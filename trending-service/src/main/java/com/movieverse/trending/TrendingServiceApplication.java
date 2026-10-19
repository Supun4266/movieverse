package com.movieverse.trending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A second, separate service. It never talks to the main backend directly:
 * it only listens to Kafka and counts how many times each movie was played.
 */
@SpringBootApplication
public class TrendingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrendingServiceApplication.class, args);
    }
}

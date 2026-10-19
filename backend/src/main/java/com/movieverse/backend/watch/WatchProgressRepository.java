package com.movieverse.backend.watch;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchProgressRepository extends JpaRepository<WatchProgress, Long> {

    Optional<WatchProgress> findByUserUsernameAndMovieId(String username, Long movieId);

    List<WatchProgress> findByUserUsernameOrderByUpdatedAtDesc(String username);
}

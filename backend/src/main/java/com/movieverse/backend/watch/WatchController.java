package com.movieverse.backend.watch;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Login required.
 *
 * POST /api/watch/{movieId}/start  -> pressed Play (also sends a Kafka event)
 * PUT  /api/watch/{movieId}        -> save progress, body: {"progressPercent": 40}
 * GET  /api/watch/{movieId}        -> how far I got in this movie
 * GET  /api/watch/continue         -> my "Continue Watching" row
 */
@RestController
@RequestMapping("/api/watch")
public class WatchController {

    private final WatchService watchService;

    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }

    @GetMapping("/continue")
    public List<WatchStatus> continueWatching(@AuthenticationPrincipal Jwt jwt) {
        return watchService.continueWatching(jwt.getSubject());
    }

    @GetMapping("/{movieId}")
    public WatchStatus getStatus(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        return watchService.getStatus(jwt.getSubject(), movieId);
    }

    @PostMapping("/{movieId}/start")
    public WatchStatus start(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        return watchService.startWatching(jwt.getSubject(), movieId);
    }

    @PutMapping("/{movieId}")
    public WatchStatus updateProgress(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId,
                                      @Valid @RequestBody ProgressRequest request) {
        return watchService.updateProgress(jwt.getSubject(), movieId, request.progressPercent());
    }
}

package com.movieverse.backend.watch;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ProgressRequest(
        @Min(value = 0, message = "Progress must be 0 to 100")
        @Max(value = 100, message = "Progress must be 0 to 100")
        int progressPercent) {
}

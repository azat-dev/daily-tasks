package com.azatdev.dailytasks.domain.models;

import java.time.LocalDate;
import java.util.UUID;

public record Backlog(
    Long id,
    UUID ownerId,
    LocalDate startDate,
    Duration duration
) {

    public enum Duration {
        DAY,
        WEEK,
    }
}

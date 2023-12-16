package com.azatdev.dailytasks.domain.models;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public record NewActivitySession(
    UUID userId,
    long taskId,
    ZonedDateTime startedAt,
    Optional<ZonedDateTime> finishedAt
) {
}

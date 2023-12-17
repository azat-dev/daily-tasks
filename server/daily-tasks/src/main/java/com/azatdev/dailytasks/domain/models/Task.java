package com.azatdev.dailytasks.domain.models;

import java.time.ZonedDateTime;
import java.util.UUID;

public record Task(
    Long id,
    UUID ownerId,
    String title,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt,
    Status status,
    Priority priority,
    String description
) {

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}

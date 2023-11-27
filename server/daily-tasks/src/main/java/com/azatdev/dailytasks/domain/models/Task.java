package com.azatdev.dailytasks.domain.models;

public record Task(
    Long id,
    String title,
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

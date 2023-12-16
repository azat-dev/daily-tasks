package com.azatdev.dailytasks.presentation.api.rest.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.azatdev.dailytasks.domain.models.Task;

public enum TaskPriorityPresentation {
    @JsonProperty("low")
    LOW,
    @JsonProperty("medium")
    MEDIUM,
    @JsonProperty("high")
    HIGH;

    public Task.Priority toDomain() {
        return switch (this) {
        case LOW -> Task.Priority.LOW;
        case MEDIUM -> Task.Priority.MEDIUM;
        case HIGH -> Task.Priority.HIGH;
        };
    }
}

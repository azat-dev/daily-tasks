package com.azatdev.dailytasks.presentation.api.rest.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.azatdev.dailytasks.domain.models.Backlog;

public enum BacklogDurationPresentation {

    @JsonProperty("day")
    DAY,
    @JsonProperty("week")
    WEEK;

    public Backlog.Duration toDomain() {
        return switch (this) {
        case DAY -> Backlog.Duration.DAY;
        case WEEK -> Backlog.Duration.WEEK;
        };
    }
}

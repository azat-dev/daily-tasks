package com.azatdev.dailytasks.domain.models;

import java.time.LocalDate;

public record Backlog(
    Long id,
    LocalDate startDate,
    Duration duration
) {

    public enum Duration {
        DAY,
        WEEK,
    }
}

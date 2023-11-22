package com.azatdev.dailytasks.domain.models;

import java.util.UUID;
import java.time.LocalDate;

public record Backlog(
    UUID id, 
    LocalDate startDate, 
    Duration duration
) {
    
    public enum Duration {
        DAY,
        WEEK,
    }
}
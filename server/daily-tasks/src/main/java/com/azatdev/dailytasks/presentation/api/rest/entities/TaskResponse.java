package com.azatdev.dailytasks.presentation.api.rest.entities;

import java.time.ZonedDateTime;

public record TaskResponse(
    Long id,
    String title,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt,
    TaskStatusPresentation status,
    TaskPriorityPresentation priority,
    String description
) {
}

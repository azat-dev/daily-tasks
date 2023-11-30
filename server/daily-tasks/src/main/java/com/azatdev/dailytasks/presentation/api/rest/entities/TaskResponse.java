package com.azatdev.dailytasks.presentation.api.rest.entities;

public record TaskResponse(
    Long id,
    String title,
    TaskStatusPresentation status,
    TaskPriorityPresentation priority,
    String description
) {
}

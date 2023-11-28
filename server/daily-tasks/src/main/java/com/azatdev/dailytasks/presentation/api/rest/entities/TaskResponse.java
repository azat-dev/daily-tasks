package com.azatdev.dailytasks.presentation.api.rest.entities;

public record TaskResponse(
    Long id,
    String title,
    TaskStatusResponse status,
    TaskPriorityResponse priority,
    String description
) {
}

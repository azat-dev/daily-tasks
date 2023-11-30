package com.azatdev.dailytasks.presentation.api.rest.entities;

public record CreateTaskInBacklogRequest(
    String title,
    TaskPriorityPresentation priority,
    String description
) {
}

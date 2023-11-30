package com.azatdev.dailytasks.presentation.api.rest.entities;

public record CreateTaskInBacklogRequest(
    String title,
    String description
) {
}

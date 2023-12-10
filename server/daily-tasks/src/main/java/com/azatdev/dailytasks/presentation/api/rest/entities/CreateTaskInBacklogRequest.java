package com.azatdev.dailytasks.presentation.api.rest.entities;

import java.util.Optional;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskInBacklogRequest(
    @NotBlank String title,
    Optional<TaskPriorityPresentation> priority,
    String description
) {
}

package com.azatdev.dailytasks.presentation.api.rest.entities;

import jakarta.validation.constraints.NotBlank;


public record CreateTaskInBacklogRequest(
    @NotBlank
    String title,
    TaskPriorityPresentation priority,
    String description
) {
}

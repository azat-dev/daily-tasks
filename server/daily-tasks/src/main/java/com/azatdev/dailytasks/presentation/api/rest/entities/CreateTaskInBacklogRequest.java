package com.azatdev.dailytasks.presentation.api.rest.entities;

import java.util.Optional;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskInBacklogRequest(
    @NotBlank String title,
    Optional<TaskPriorityPresentation> priority,
    @Length(max = 10000) String description
) {
}

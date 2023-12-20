package com.azatdev.dailytasks.presentation.api.rest.entities;

import java.util.Optional;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequest(
    @NotBlank @Length(max = 1000) String title,
    @Length(max = 10000) String description,
    Optional<TaskPriorityPresentation> priority
) {
}

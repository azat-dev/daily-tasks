package com.azatdev.dailytasks.domain.models;

import jakarta.annotation.Nonnull;

public record NewTaskData(
    @Nonnull String title,
    Task.Priority priority,
    @Nonnull String description
) {
}

package com.azatdev.dailytasks.domain.models;

import java.util.Optional;

public record NewTaskData(
    String title,
    Optional<Task.Priority> priority,
    String description
) {
}

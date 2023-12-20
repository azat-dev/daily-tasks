package com.azatdev.dailytasks.domain.models;

import java.util.Optional;

public record EditTaskData(
    String title,
    Optional<Task.Priority> priority,
    String description
) {
}

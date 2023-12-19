package com.azatdev.dailytasks.domain.models;

import java.util.Optional;

public record EditTaskData(
    Optional<String> title,
    Optional<Optional<Task.Priority>> priority,
    Optional<String> description
) {
}

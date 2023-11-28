package com.azatdev.dailytasks.presentation.api.rest.entities.utils;

import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;

@FunctionalInterface
public interface MapTaskToResponse {

    public TaskResponse map(Task task);
}

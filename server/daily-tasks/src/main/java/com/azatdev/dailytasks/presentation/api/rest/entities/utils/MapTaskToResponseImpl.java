package com.azatdev.dailytasks.presentation.api.rest.entities.utils;

import org.springframework.stereotype.Component;

import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskPriorityResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskStatusResponse;

@Component
public class MapTaskToResponseImpl implements MapTaskToResponse {

    private TaskStatusResponse mapStatus(Task.Status status) {
        return switch (status) {
        case COMPLETED -> TaskStatusResponse.COMPLETED;
        case IN_PROGRESS -> TaskStatusResponse.IN_PROGRESS;
        case NOT_STARTED -> TaskStatusResponse.NOT_STARTED;
        };
    }

    private TaskPriorityResponse mapPriority(Task.Priority priority) {
        if (priority == null) {
            return null;
        }

        return switch (priority) {
        case HIGH -> TaskPriorityResponse.HIGH;
        case LOW -> TaskPriorityResponse.LOW;
        case MEDIUM -> TaskPriorityResponse.MEDIUM;
        };
    }

    @Override
    public TaskResponse map(Task task) {
        return new TaskResponse(
            task.id(),
            task.title(),
            mapStatus(task.status()),
            mapPriority(task.priority()),
            task.description()
        );
    }
}

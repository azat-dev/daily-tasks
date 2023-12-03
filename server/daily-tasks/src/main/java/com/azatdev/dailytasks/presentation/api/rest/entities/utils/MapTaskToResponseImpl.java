package com.azatdev.dailytasks.presentation.api.rest.entities.utils;

import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskPriorityPresentation;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskStatusPresentation;

public class MapTaskToResponseImpl implements MapTaskToResponse {

    private TaskStatusPresentation mapStatus(Task.Status status) {
        return switch (status) {
        case COMPLETED -> TaskStatusPresentation.COMPLETED;
        case IN_PROGRESS -> TaskStatusPresentation.IN_PROGRESS;
        case NOT_STARTED -> TaskStatusPresentation.NOT_STARTED;
        };
    }

    private TaskPriorityPresentation mapPriority(Task.Priority priority) {
        if (priority == null) {
            return null;
        }

        return switch (priority) {
        case HIGH -> TaskPriorityPresentation.HIGH;
        case LOW -> TaskPriorityPresentation.LOW;
        case MEDIUM -> TaskPriorityPresentation.MEDIUM;
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

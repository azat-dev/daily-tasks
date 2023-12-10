package com.azatdev.dailytasks.data.repositories.data;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.domain.models.Task;

import jakarta.annotation.Nonnull;

public class MapTaskDataToDomainImpl implements MapTaskDataToDomain {

    // Constructors

    public MapTaskDataToDomainImpl() {
    }

    // Methods

    private Task.Priority mapPriority(@Nonnull TaskData.Priority priority) {

        if (priority == null) {
            return null;
        }

        switch (priority) {
        case LOW:
            return Task.Priority.LOW;
        case MEDIUM:
            return Task.Priority.MEDIUM;
        case HIGH:
            return Task.Priority.HIGH;
        default:
            throw new IllegalArgumentException("Unknown priority: " + priority);
        }
    }

    private Task.Status mapStatus(TaskData.Status status) {
        switch (status) {
        case NOT_STARTED:
            return Task.Status.NOT_STARTED;
        case IN_PROGRESS:
            return Task.Status.IN_PROGRESS;
        case COMPLETED:
            return Task.Status.COMPLETED;
        default:
            throw new IllegalArgumentException("Unknown status: " + status);
        }
    }

    @Override
    @Nonnull
    public Task execute(TaskData taskData) {
        return new Task(
            taskData.getId(),
            taskData.getTitle(),
            taskData.getCreatedAt(),
            taskData.getUpdatedAt(),
            this.mapStatus(taskData.getStatus()),
            this.mapPriority(taskData.getPriority()),
            taskData.getDescription()
        );
    }
}

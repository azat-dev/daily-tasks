package com.azatdev.dailytasks.domain.usecases;

import java.time.ZonedDateTime;
import java.util.UUID;

@FunctionalInterface
public interface StopTaskUseCase {
    ZonedDateTime execute(
        UUID userId,
        long taskId
    ) throws RuntimeException;

    class TaskAlreadyStoppedException extends RuntimeException {

        private long taskId;

        public TaskAlreadyStoppedException(long taskId) {
            super("Task with id " + taskId + " is already stopped");
            this.taskId = taskId;
        }

        long getTaskId() {
            return taskId;
        }
    }
}

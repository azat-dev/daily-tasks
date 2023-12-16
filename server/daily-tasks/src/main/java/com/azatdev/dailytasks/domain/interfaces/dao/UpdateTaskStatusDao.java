package com.azatdev.dailytasks.domain.interfaces.dao;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
public interface UpdateTaskStatusDao {

    Void execute(
        UUID userId,
        Long taskId,
        Task.Status newStatus,
        Optional<Transaction> transaction
    ) throws TaskNotFoundException;

    class TaskNotFoundException extends RuntimeException {
        private long taskId;

        public TaskNotFoundException(long taskId) {
            super("Task not found: " + taskId);
            this.taskId = taskId;
        }

        public long getTaskId() {
            return taskId;
        }
    }
}

package com.azatdev.dailytasks.domain.interfaces.repositories.tasks;

import java.util.Optional;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;

public interface TasksRepositoryCreate {

    // Methods

    Task createTask(
        long backlogId,
        NewTaskData newTaskData,
        Optional<Transaction> transaction
    ) throws BacklogNotFoundException;

    // Exceptions

    final class BacklogNotFoundException extends Exception {
        private final long backlogId;

        public BacklogNotFoundException(long backlogId) {
            super("Backlog with id " + backlogId + " not found");
            this.backlogId = backlogId;
        }

        public long getBacklogId() {
            return backlogId;
        }
    }
}

package com.azatdev.dailytasks.domain.interfaces.repositories.backlog;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Backlog;

public interface BacklogRepositoryCreate {

    public long create(
        UUID ownerId,
        LocalDate startDate,
        Backlog.Duration duration,
        Optional<Transaction> transaction
    ) throws BacklogAlreadyExistsException;

    // Exceptions

    public class BacklogAlreadyExistsException extends Exception {
        private final long backlogId;

        public BacklogAlreadyExistsException(long backlogId) {
            this.backlogId = backlogId;
        }

        public long getBacklogId() {
            return backlogId;
        }
    }
}

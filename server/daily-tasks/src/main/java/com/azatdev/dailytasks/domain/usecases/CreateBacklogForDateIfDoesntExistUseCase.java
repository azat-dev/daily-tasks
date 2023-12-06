package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Backlog;

public interface CreateBacklogForDateIfDoesntExistUseCase {

    /**
     * Adds backlog for given date and duration if it does not exist.
     * 
     * @param date
     * @param backlogDuration
     * @return Backlog Id
     */
    public long execute(
        UUID ownerId,
        LocalDate date,
        Backlog.Duration backlogDuration,
        Optional<Transaction> transaction
    );
}

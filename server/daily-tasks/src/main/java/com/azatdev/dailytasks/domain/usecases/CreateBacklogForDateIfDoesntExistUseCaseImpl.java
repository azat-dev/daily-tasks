package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryCreate.BacklogAlreadyExistsException;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

public class CreateBacklogForDateIfDoesntExistUseCaseImpl implements CreateBacklogForDateIfDoesntExistUseCase {

    final private AdjustDateToStartOfBacklog adjustDateToStartOfBacklog;
    final private BacklogRepositoryCreate backlogRepository;

    public CreateBacklogForDateIfDoesntExistUseCaseImpl(
        AdjustDateToStartOfBacklog adjustDateToStartOfBacklog,
        BacklogRepositoryCreate backlogRepository
    ) {
        this.adjustDateToStartOfBacklog = adjustDateToStartOfBacklog;
        this.backlogRepository = backlogRepository;
    }

    @Override
    public long execute(
        UUID ownerId,
        LocalDate date,
        Backlog.Duration backlogDuration,
        Optional<Transaction> transaction
    ) {

        final var backlogStartTime = adjustDateToStartOfBacklog.calculate(
            date,
            backlogDuration
        );

        try {
            return backlogRepository.create(
                UUID.randomUUID(),
                backlogStartTime,
                backlogDuration,
                transaction
            );
        } catch (BacklogAlreadyExistsException e) {
            return e.getBacklogId();
        }
    }

}

package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.Optional;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

public class CreateBacklogForDateIfDoesntExistUseCaseImpl implements CreateBacklogForDateIfDoesntExistUseCase {

    final private AdjustDateToStartOfBacklog adjustDateToStartOfBacklog;
    final private BacklogRepositoryGet backlogRepository;

    public CreateBacklogForDateIfDoesntExistUseCaseImpl(
        AdjustDateToStartOfBacklog adjustDateToStartOfBacklog,
        BacklogRepositoryGet backlogRepository
    ) {
        this.adjustDateToStartOfBacklog = adjustDateToStartOfBacklog;
        this.backlogRepository = backlogRepository;
    }

    @Override
    public long execute(
        LocalDate date,
        Backlog.Duration backlogDuration,
        Optional<Transaction> transaction
    ) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}

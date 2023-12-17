package com.azatdev.dailytasks.data.repositories.data;

import java.time.ZonedDateTime;
import java.util.Optional;

import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.exceptions.TaskAlreadyStoppedException;
import com.azatdev.dailytasks.domain.interfaces.dao.StopActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;

public final class StopActivitySessionDaoImpl implements StopActivitySessionDao {

    private final JpaActivitySessionsRepository activitySessionsRepository;

    public StopActivitySessionDaoImpl(JpaActivitySessionsRepository activitySessionsRepository) {
        this.activitySessionsRepository = activitySessionsRepository;
    }

    @Override
    public void execute(
        long activitySessionId,
        ZonedDateTime finishedAt,
        Optional<Transaction> transaction
    ) throws TaskAlreadyStoppedException {

        final var result = activitySessionsRepository.findById(activitySessionId);

        if (result.isEmpty()) {
            throw new TaskAlreadyStoppedException(activitySessionId);
        }

        final var activitySession = result.get();
        activitySession.setFinishedAt(Optional.of(finishedAt));

        activitySessionsRepository.saveAndFlush(activitySession);
    }
}

package com.azatdev.dailytasks.domain.usecases;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.utils.CurrentTimeProvider;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

public final class StartTaskUseCaseImpl implements StartTaskUseCase {

    private CurrentTimeProvider currentTimeProvider;
    private GetRunningActivitySessionForTaskDao getCurrentActivitySessionDao;
    private AddNewActivitySessionDao addNewActivitySessionDao;

    public StartTaskUseCaseImpl(
        CurrentTimeProvider currentTimeProvider,
        GetRunningActivitySessionForTaskDao getCurrentActivitySessionDao,
        AddNewActivitySessionDao addNewActivitySessionDao
    ) {
        this.currentTimeProvider = currentTimeProvider;
        this.getCurrentActivitySessionDao = getCurrentActivitySessionDao;
        this.addNewActivitySessionDao = addNewActivitySessionDao;
    }

    @Override
    public ZonedDateTime execute(
        UUID userId,
        Long taskId
    ) {

        final var currentActivitySession = getCurrentActivitySessionDao.execute(
            userId,
            taskId
        );

        if (currentActivitySession.isPresent()) {
            return currentActivitySession.get()
                .startedAt();
        }

        final var currentTime = currentTimeProvider.execute();
        addNewActivitySessionDao.execute(
            new NewActivitySession(
                userId,
                taskId,
                currentTime,
                Optional.empty()
            )
        );

        return currentTime;
    }
}

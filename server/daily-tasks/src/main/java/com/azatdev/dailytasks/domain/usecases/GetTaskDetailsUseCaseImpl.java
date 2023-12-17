package com.azatdev.dailytasks.domain.usecases;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;
import com.azatdev.dailytasks.domain.models.Task;

public class GetTaskDetailsUseCaseImpl implements GetTaskDetailsUseCase {

    private GetTaskDao getTaskDao;

    public GetTaskDetailsUseCaseImpl(GetTaskDao getTaskDao) {
        this.getTaskDao = getTaskDao;
    }

    @Override
    public Optional<Task> execute(
        UUID userId,
        long taskId
    ) {
        return getTaskDao.execute(
            userId,
            taskId
        );
    }
}

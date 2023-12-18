package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.BacklogNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetBacklogByIdDao;

public class CanUserViewBacklogUseCaseImpl implements CanUserViewBacklogUseCase {

    final GetBacklogByIdDao getBacklogByIdDao;

    public CanUserViewBacklogUseCaseImpl(GetBacklogByIdDao getBacklogByIdDao) {
        this.getBacklogByIdDao = getBacklogByIdDao;
    }

    @Override
    public boolean execute(
        UUID userId,
        long backlogId
    ) throws BacklogNotFoundException {

        final var backlog = getBacklogByIdDao.execute(backlogId)
            .orElseThrow(() -> new BacklogNotFoundException(backlogId));

        return backlog.ownerId()
            .equals(userId);
    }
}

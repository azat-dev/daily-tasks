package com.azatdev.dailytasks.data.dao;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.data.repositories.data.MapActivitySessionToDomain;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.models.ActivitySession;

final class GetRunningActivitySessionForTaskDaoImpl implements GetRunningActivitySessionForTaskDao {

    private JpaActivitySessionsRepository repository;
    private MapActivitySessionToDomain mapper;

    public GetRunningActivitySessionForTaskDaoImpl(
        JpaActivitySessionsRepository repository,
        MapActivitySessionToDomain mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ActivitySession> execute(
        UUID userId,
        long taskId
    ) {
        final var existingActivitySession = repository.findByOwnerIdAndTaskIdAndFinishedAt(
            userId,
            taskId,
            Optional.empty()
        );

        return existingActivitySession.map(mapper::map);
    }
}

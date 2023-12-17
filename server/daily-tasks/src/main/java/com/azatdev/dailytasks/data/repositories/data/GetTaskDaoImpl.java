package com.azatdev.dailytasks.data.repositories.data;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;
import com.azatdev.dailytasks.domain.models.Task;

public final class GetTaskDaoImpl implements GetTaskDao {

    private final MapTaskDataToDomain mapTaskDataToDomain;
    private final JpaTasksRepository tasksRepository;

    public GetTaskDaoImpl(
        MapTaskDataToDomain mapper,
        JpaTasksRepository tasksRepository
    ) {
        this.mapTaskDataToDomain = mapper;
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Optional<Task> execute(
        UUID userId,
        long taskId
    ) {
        return tasksRepository.findByOwnerIdAndId(
            userId,
            taskId
        )
            .map(mapTaskDataToDomain::execute);
    }
}

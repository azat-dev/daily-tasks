package com.azatdev.dailytasks.domain.usecases;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomain;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.models.Task;

public final class GetTaskDetailsUseCaseImpl implements GetTaskDetailsUseCase {

    private final MapTaskDataToDomain mapTaskDataToDomain;
    private final JpaTasksRepository tasksRepository;

    public GetTaskDetailsUseCaseImpl(
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

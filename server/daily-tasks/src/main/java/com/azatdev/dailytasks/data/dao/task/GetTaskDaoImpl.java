package com.azatdev.dailytasks.data.dao.task;

import java.util.Optional;

import com.azatdev.dailytasks.data.jpa.JpaTasksRepository;
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
    public Optional<Task> execute(long taskId) {
        return tasksRepository.findById(taskId)
            .map(mapTaskDataToDomain::execute);
    }
}

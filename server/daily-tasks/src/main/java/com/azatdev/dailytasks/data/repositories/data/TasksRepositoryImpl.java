package com.azatdev.dailytasks.data.repositories.data;

import java.util.ArrayList;
import java.util.List;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPATasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

public class TasksRepositoryImpl implements TasksRepositoryList {

    // Fields

    private final JPATasksRepository jpaTasksRepository;
    private final MapTaskDataToDomain mapTaskDataToDomain;

    // Constructors

    public TasksRepositoryImpl(
        JPATasksRepository jpaTasksRepository,
        MapTaskDataToDomain mapTaskDataToDomain
    ) {
        this.jpaTasksRepository = jpaTasksRepository;
        this.mapTaskDataToDomain = mapTaskDataToDomain;
    }

    // Methods

    @Override
    public Result<List<Task>, Error> list(Long backlogId) {

        final Iterable<TaskData> items = jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId);

        final List<Task> tasks = new ArrayList<>();

        for (TaskData item : items) {
            tasks.add(mapTaskDataToDomain.map(item));
        }

        return Result.success(tasks);
    }
}
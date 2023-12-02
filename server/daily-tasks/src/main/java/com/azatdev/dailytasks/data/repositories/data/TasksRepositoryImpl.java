package com.azatdev.dailytasks.data.repositories.data;

import java.util.ArrayList;
import java.util.List;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPATasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

public class TasksRepositoryImpl implements TasksRepository {

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
    public Result<List<Task>, TasksRepositoryList.Error> list(Long backlogId) {

        final Iterable<TaskData> items = jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId);

        final List<Task> tasks = new ArrayList<>();

        for (TaskData item : items) {
            tasks.add(mapTaskDataToDomain.map(item));
        }

        return Result.success(tasks);
    }

    private static TaskData.Priority map(Task.Priority priority) {
        if (priority == null) {
            return null;
        }

        return switch (priority) {
        case LOW -> TaskData.Priority.LOW;
        case MEDIUM -> TaskData.Priority.MEDIUM;
        case HIGH -> TaskData.Priority.HIGH;
        };
    }

    @Override
    public Result<Task, TasksRepositoryCreate.Error> createTask(
        long backlogId,
        NewTaskData newTaskData,
        Transaction transaction
    ) {

        try {

            final var lastOrderInBacklog = jpaTasksRepository
                .findFirstOrderInBacklogByBacklogIdOrderByOrderInBacklogDesc(backlogId);

            int orderInBacklog = 0;

            if (lastOrderInBacklog.isPresent()) {
                orderInBacklog = lastOrderInBacklog.get()
                    .getOrderInBacklog() + 1;
            }

            final var taskData = new TaskData(
                backlogId,
                orderInBacklog,
                newTaskData.title(),
                newTaskData.description(),
                TaskData.Status.NOT_STARTED,
                map(newTaskData.priority())
            );

            final var savedTaskData = jpaTasksRepository.saveAndFlush(taskData);

            if (savedTaskData == null) {
                return Result.failure(TasksRepositoryCreate.Error.INTERNAL_ERROR);
            }

            return Result.success(mapTaskDataToDomain.map(savedTaskData));

        } catch (Exception e) {

            return Result.failure(TasksRepositoryCreate.Error.INTERNAL_ERROR);
        }
    }
}

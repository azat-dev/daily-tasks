package com.azatdev.dailytasks.data.repositories.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPATasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;

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
    public List<Task> list(long backlogId) {

        final var items = jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId);

        final var tasks = new ArrayList<Task>(items.size());

        for (TaskData item : items) {
            tasks.add(mapTaskDataToDomain.map(item));
        }

        return tasks;
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
    public Task createTask(
        long backlogId,
        NewTaskData newTaskData,
        Optional<Transaction> transaction
    ) {

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
        return mapTaskDataToDomain.map(savedTaskData);
    }
}

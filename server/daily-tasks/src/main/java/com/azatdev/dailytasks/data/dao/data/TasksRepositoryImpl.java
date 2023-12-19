package com.azatdev.dailytasks.data.dao.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.data.dao.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaGetBacklogReference;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaGetUserReference;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;

public class TasksRepositoryImpl implements TasksRepository {

    // Fields

    private final JpaGetUserReference getUserReference;
    private final JpaGetBacklogReference getBacklogReference;
    private final JpaTasksRepository jpaTasksRepository;
    private final MapTaskDataToDomain mapTaskDataToDomain;

    // Constructors

    public TasksRepositoryImpl(
        JpaGetUserReference getUserReference,
        JpaGetBacklogReference getBacklogReference,
        JpaTasksRepository jpaTasksRepository,
        MapTaskDataToDomain mapTaskDataToDomain
    ) {
        this.getUserReference = getUserReference;
        this.getBacklogReference = getBacklogReference;
        this.jpaTasksRepository = jpaTasksRepository;
        this.mapTaskDataToDomain = mapTaskDataToDomain;
    }

    // Methods

    @Override
    public List<Task> list(
        UUID ownerId,
        long backlogId
    ) {

        final var items = jpaTasksRepository.findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc(
            ownerId,
            backlogId
        );

        final var tasks = new ArrayList<Task>(items.size());

        for (TaskData item : items) {
            tasks.add(mapTaskDataToDomain.execute(item));
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
        UUID ownerId,
        long backlogId,
        NewTaskData newTaskData,
        Optional<Transaction> transaction
    ) {

        final var lastOrderInBacklog = jpaTasksRepository
            .findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc(
                ownerId,
                backlogId
            );

        int orderInBacklog = 0;

        if (lastOrderInBacklog.isPresent()) {
            orderInBacklog = lastOrderInBacklog.get()
                .getOrderInBacklog() + 1;
        }

        final var taskData = new TaskData(
            getUserReference.execute(ownerId),
            getBacklogReference.execute(backlogId),
            orderInBacklog,
            newTaskData.title(),
            newTaskData.description(),
            TaskData.Status.NOT_STARTED,
            newTaskData.priority()
                .map(p -> map(p))
                .orElse(null)
        );

        final var savedTaskData = jpaTasksRepository.saveAndFlush(taskData);
        return mapTaskDataToDomain.execute(savedTaskData);
    }
}

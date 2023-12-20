package com.azatdev.dailytasks.data.dao.task;

import com.azatdev.dailytasks.data.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.models.EditTaskData;

public final class UpdateTaskDaoImpl implements UpdateTaskDao {

    private final JpaTasksRepository tasksRepository;

    public UpdateTaskDaoImpl(JpaTasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Override
    public void execute(
        long taskId,
        EditTaskData data
    ) throws TaskNotFoundException {

        final var taskData = tasksRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));

        taskData.setTitle(data.title());

        taskData.setDescription(data.description());

        final var priorityMapper = new MapTaskPriorityToData();

        taskData.setPriority(
            data.priority()
                .map(priorityMapper::map)
        );

        tasksRepository.saveAndFlush(taskData);
    }
}

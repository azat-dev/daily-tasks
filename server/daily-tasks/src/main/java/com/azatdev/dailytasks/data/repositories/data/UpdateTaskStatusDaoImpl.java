package com.azatdev.dailytasks.data.repositories.data;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.UpdateTaskStatusDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Task;

public final class UpdateTaskStatusDaoImpl implements UpdateTaskStatusDao {

    private JpaTasksRepository tasksRepository;

    public UpdateTaskStatusDaoImpl(JpaTasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Void execute(
        UUID userId,
        Long taskId,
        Task.Status newStatus,
        Optional<Transaction> transaction
    ) {

        final var taskResult = tasksRepository.findById(taskId);

        if (taskResult.isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }

        final var taskData = taskResult.get();

        final var statusMapper = new MapTaskStatusToData();

        taskData.setStatus(statusMapper.map(newStatus));
        tasksRepository.saveAndFlush(taskData);
        return null;
    }
}

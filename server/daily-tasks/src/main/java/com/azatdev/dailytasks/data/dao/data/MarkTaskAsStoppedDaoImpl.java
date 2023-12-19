package com.azatdev.dailytasks.data.dao.data;

import java.time.ZonedDateTime;
import java.util.Optional;

import com.azatdev.dailytasks.data.dao.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.MarkTaskAsStoppedDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;

public final class MarkTaskAsStoppedDaoImpl implements MarkTaskAsStoppedDao {

    private final JpaTasksRepository tasksRepository;

    public MarkTaskAsStoppedDaoImpl(JpaTasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Void execute(
        long taskId,
        ZonedDateTime finishedAt,
        Optional<Transaction> transaction
    ) throws TaskNotFoundException {

        final var result = tasksRepository.findById(taskId);

        if (result.isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }

        final var task = result.get();

        task.setStatus(TaskData.Status.NOT_STARTED);
        tasksRepository.saveAndFlush(task);
        return null;
    }
}

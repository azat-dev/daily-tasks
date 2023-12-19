package com.azatdev.dailytasks.data.dao.data;

import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.DeleteTaskDao;

public final class DeleteTaskDaoImpl implements DeleteTaskDao {

    private final JpaTasksRepository tasksRepository;

    public DeleteTaskDaoImpl(JpaTasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Override
    public void execute(long taskId) throws TaskNotFoundException {

        final var task = tasksRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));

        tasksRepository.delete(task);
        tasksRepository.flush();
    }
}

package com.azatdev.dailytasks.domain.interfaces.repositories.tasks;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.azatdev.dailytasks.domain.models.Task;

@Repository
public interface TasksRepositoryList {

    public List<Task> list(
        UUID ownerId,
        long backlogId
    );
}

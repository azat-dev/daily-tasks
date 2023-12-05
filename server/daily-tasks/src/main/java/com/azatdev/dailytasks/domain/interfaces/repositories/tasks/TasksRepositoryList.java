package com.azatdev.dailytasks.domain.interfaces.repositories.tasks;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.azatdev.dailytasks.domain.models.Task;

@Repository
public interface TasksRepositoryList {

    public List<Task> list(long backlogId);
}

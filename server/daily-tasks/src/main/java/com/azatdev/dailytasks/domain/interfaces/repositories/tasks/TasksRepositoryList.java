package com.azatdev.dailytasks.domain.interfaces.repositories.tasks;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

@Repository
public interface TasksRepositoryList {

    public enum Error {
        INTERNAL_ERROR
    }

    public Result<List<Task>, Error> list(Long backlogId);
}

package com.azatdev.dailytasks.data.repositories.data;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
public interface MapTaskDataToDomain {

    Task execute(TaskData taskData);
}

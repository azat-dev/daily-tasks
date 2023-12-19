package com.azatdev.dailytasks.data.dao.task;

import com.azatdev.dailytasks.data.entities.TaskData;
import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
public interface MapTaskDataToDomain {

    Task execute(TaskData taskData);
}

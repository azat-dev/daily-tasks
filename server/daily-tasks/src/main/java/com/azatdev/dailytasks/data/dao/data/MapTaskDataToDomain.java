package com.azatdev.dailytasks.data.dao.data;

import com.azatdev.dailytasks.data.dao.persistence.entities.TaskData;
import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
public interface MapTaskDataToDomain {

    Task execute(TaskData taskData);
}

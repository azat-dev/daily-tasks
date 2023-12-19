package com.azatdev.dailytasks.data.dao.task;

import com.azatdev.dailytasks.data.entities.TaskData;
import com.azatdev.dailytasks.domain.models.Task;

public final class MapTaskPriorityToData {

    public TaskData.Priority map(Task.Priority taskPriority) {

        return switch (taskPriority) {
        case LOW -> TaskData.Priority.LOW;
        case MEDIUM -> TaskData.Priority.MEDIUM;
        case HIGH -> TaskData.Priority.HIGH;
        };
    }
}

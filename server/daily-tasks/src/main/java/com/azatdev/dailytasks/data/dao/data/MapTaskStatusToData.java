package com.azatdev.dailytasks.data.dao.data;

import com.azatdev.dailytasks.data.dao.persistence.entities.TaskData;
import com.azatdev.dailytasks.domain.models.Task;

public final class MapTaskStatusToData {

    public TaskData.Status map(Task.Status taskStatus) {

        return switch (taskStatus) {
        case NOT_STARTED -> TaskData.Status.NOT_STARTED;
        case IN_PROGRESS -> TaskData.Status.IN_PROGRESS;
        case COMPLETED -> TaskData.Status.COMPLETED;
        };
    }
}

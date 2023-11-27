package com.azatdev.dailytasks.data.repositories.data;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.domain.models.Task;

import io.micrometer.common.lang.NonNull;

@FunctionalInterface
public interface MapTaskDataToDomain {

    @NonNull public Task map(@NonNull TaskData taskData);
}

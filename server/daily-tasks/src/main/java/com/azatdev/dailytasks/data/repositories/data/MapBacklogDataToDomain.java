package com.azatdev.dailytasks.data.repositories.data;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.domain.models.Backlog;

@FunctionalInterface
public interface MapBacklogDataToDomain {
    Backlog map(BacklogData backlogData);
}

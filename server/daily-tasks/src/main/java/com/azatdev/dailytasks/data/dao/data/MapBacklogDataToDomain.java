package com.azatdev.dailytasks.data.dao.data;

import com.azatdev.dailytasks.data.dao.persistence.entities.BacklogData;
import com.azatdev.dailytasks.domain.models.Backlog;

@FunctionalInterface
public interface MapBacklogDataToDomain {
    Backlog map(BacklogData backlogData);
}

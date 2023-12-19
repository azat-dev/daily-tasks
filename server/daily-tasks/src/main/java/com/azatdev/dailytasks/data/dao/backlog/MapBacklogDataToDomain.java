package com.azatdev.dailytasks.data.dao.backlog;

import com.azatdev.dailytasks.data.entities.BacklogData;
import com.azatdev.dailytasks.domain.models.Backlog;

@FunctionalInterface
public interface MapBacklogDataToDomain {
    Backlog map(BacklogData backlogData);
}

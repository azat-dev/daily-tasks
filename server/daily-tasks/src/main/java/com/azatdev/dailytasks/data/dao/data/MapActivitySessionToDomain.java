package com.azatdev.dailytasks.data.dao.data;

import com.azatdev.dailytasks.data.dao.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.domain.models.ActivitySession;

@FunctionalInterface
public interface MapActivitySessionToDomain {
    ActivitySession map(ActivitySessionData data);
}

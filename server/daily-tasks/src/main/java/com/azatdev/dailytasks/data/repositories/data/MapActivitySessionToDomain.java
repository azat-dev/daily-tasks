package com.azatdev.dailytasks.data.repositories.data;

import com.azatdev.dailytasks.data.repositories.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.domain.models.ActivitySession;

@FunctionalInterface
public interface MapActivitySessionToDomain {
    ActivitySession map(ActivitySessionData data);
}

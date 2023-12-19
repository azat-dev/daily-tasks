package com.azatdev.dailytasks.data.dao.activitysession;

import com.azatdev.dailytasks.data.entities.ActivitySessionData;
import com.azatdev.dailytasks.domain.models.ActivitySession;

@FunctionalInterface
public interface MapActivitySessionToDomain {
    ActivitySession map(ActivitySessionData data);
}

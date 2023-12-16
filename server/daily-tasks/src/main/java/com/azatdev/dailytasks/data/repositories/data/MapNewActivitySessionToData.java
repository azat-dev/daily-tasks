package com.azatdev.dailytasks.data.repositories.data;

import com.azatdev.dailytasks.data.repositories.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

public interface MapNewActivitySessionToData {
    ActivitySessionData map(NewActivitySession activitySession);
}

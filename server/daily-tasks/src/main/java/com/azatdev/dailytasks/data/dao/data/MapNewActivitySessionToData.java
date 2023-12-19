package com.azatdev.dailytasks.data.dao.data;

import com.azatdev.dailytasks.data.dao.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

public interface MapNewActivitySessionToData {
    ActivitySessionData map(NewActivitySession activitySession);
}

package com.azatdev.dailytasks.data.dao.activitysession;

import com.azatdev.dailytasks.data.entities.ActivitySessionData;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

public interface MapNewActivitySessionToData {
    ActivitySessionData map(NewActivitySession activitySession);
}

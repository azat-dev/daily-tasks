package com.azatdev.dailytasks.domain.interfaces.dao;

import com.azatdev.dailytasks.domain.models.ActivitySession;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

@FunctionalInterface
public interface AddNewActivitySessionDao {
    ActivitySession execute(NewActivitySession newActivitySession);
}

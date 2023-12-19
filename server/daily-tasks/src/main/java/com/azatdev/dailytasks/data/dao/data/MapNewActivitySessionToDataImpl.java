package com.azatdev.dailytasks.data.dao.data;

import com.azatdev.dailytasks.data.dao.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaGetTaskReference;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaGetUserReference;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

public final class MapNewActivitySessionToDataImpl implements MapNewActivitySessionToData {

    private JpaGetUserReference jpaGetUserReference;
    private JpaGetTaskReference jpaGetTaskReference;

    public MapNewActivitySessionToDataImpl(
        JpaGetUserReference jpaGetUserReference,
        JpaGetTaskReference jpaGetTaskReference
    ) {
        this.jpaGetUserReference = jpaGetUserReference;
        this.jpaGetTaskReference = jpaGetTaskReference;
    }

    @Override
    public ActivitySessionData map(NewActivitySession activitySession) {

        final var ownerReference = this.jpaGetUserReference.execute(activitySession.userId());
        final var taskReference = this.jpaGetTaskReference.execute(activitySession.taskId());

        return new ActivitySessionData(
            ownerReference,
            taskReference,
            activitySession.startedAt(),
            activitySession.finishedAt()
                .orElse(null)
        );
    }
}

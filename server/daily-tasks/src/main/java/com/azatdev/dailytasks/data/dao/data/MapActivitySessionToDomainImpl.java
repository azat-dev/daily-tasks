package com.azatdev.dailytasks.data.dao.data;

import java.util.Optional;

import com.azatdev.dailytasks.data.dao.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.domain.models.ActivitySession;

public final class MapActivitySessionToDomainImpl implements MapActivitySessionToDomain {

    @Override
    public ActivitySession map(ActivitySessionData data) {

        return new ActivitySession(
            Optional.of(data.getId()),
            data.getOwner()
                .getId(),
            data.getTask()
                .getId(),
            data.getStartedAt(),
            Optional.ofNullable(data.getFinishedAt())
        );
    }
}

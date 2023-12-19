package com.azatdev.dailytasks.data.dao.backlog;

import com.azatdev.dailytasks.data.entities.BacklogData;
import com.azatdev.dailytasks.domain.models.Backlog;

public final class MapBacklogDataToDomainImpl implements MapBacklogDataToDomain {

    private Backlog.Duration mapDuration(BacklogData.Duration duration) {

        switch (duration) {
        case DAY:
            return Backlog.Duration.DAY;
        case WEEK:
            return Backlog.Duration.WEEK;
        default:
            throw new IllegalArgumentException("Unknown backlog duration: " + duration);
        }
    }

    @Override
    public Backlog map(BacklogData backlogData) {

        return new Backlog(
            backlogData.getId(),
            backlogData.getOwner()
                .getId(),
            backlogData.getStartDate(),
            mapDuration(backlogData.getDuration())
        );
    }
}

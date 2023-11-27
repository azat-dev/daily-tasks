package com.azatdev.dailytasks.data.repositories.persistence.backlog;

import java.time.LocalDate;
import java.util.Optional;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPABacklogRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.utils.Result;

public class BacklogRepositoryImpl implements BacklogRepositoryGet {

    private final JPABacklogRepository jpaBacklogRepository;

    public BacklogRepositoryImpl(JPABacklogRepository jpaBacklogRepository) {
        this.jpaBacklogRepository = jpaBacklogRepository;
    }

    private BacklogData.Duration mapDuration(Backlog.Duration duration) {

        switch (duration) {
        case DAY:
            return BacklogData.Duration.DAY;
        case WEEK:
            return BacklogData.Duration.WEEK;
        default:
            throw new IllegalArgumentException("Unknown duration");
        }
    }

    @Override
    public Result<Optional<Long>, BacklogRepositoryGet.Error> getBacklogId(
        LocalDate startDate,
        Backlog.Duration duration
    ) {

        var backlogData = jpaBacklogRepository.findByStartDateAndDuration(
            startDate,
            this.mapDuration(duration)
        );

        if (backlogData != null) {
            return Result.success(Optional.of(backlogData.getId()));
        }

        return Result.success(Optional.empty());
    }
}

package com.azatdev.dailytasks.data.repositories.persistence.backlog;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Result<Optional<UUID>, BacklogRepositoryGet.Error> getBacklogId(
        LocalDate startDate, 
        Backlog.Duration duration
    ) {
        jpaBacklogRepository.findByStartDateAndDuration(startDate, BacklogData.Duration.DAY);
        return Result.success(Optional.empty());
    }
}
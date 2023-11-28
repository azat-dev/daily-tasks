package com.azatdev.dailytasks.domain.interfaces.repositories.backlog;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.utils.Result;

@Repository
public interface BacklogRepositoryGet {

    public enum Error {
        INTERNAL_ERROR
    }

    public Result<Optional<Long>, Error> getBacklogId(
        LocalDate startDate,
        Backlog.Duration duration
    );
}

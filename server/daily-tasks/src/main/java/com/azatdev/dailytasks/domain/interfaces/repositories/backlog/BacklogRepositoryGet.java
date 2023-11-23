package com.azatdev.dailytasks.domain.interfaces.repositories.backlog;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.utils.Result;

public interface BacklogRepositoryGet {

    enum Error {
        INTERNAL_ERROR
    }

    Result<Optional<UUID>, Error> getBacklogId(LocalDate startDate, Backlog.Duration duration);
}

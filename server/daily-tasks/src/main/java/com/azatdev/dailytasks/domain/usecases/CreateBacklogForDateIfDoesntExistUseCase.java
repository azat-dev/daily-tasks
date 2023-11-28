package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.utils.Result;

public interface CreateBacklogForDateIfDoesntExistUseCase {

    public enum Error {
        INTERNAL_ERROR
    }

    /**
     * Adds backlog for given date and duration if it does not exist.
     * 
     * @param date
     * @param backlogDuration
     * @return Backlog Id
     */
    public Result<Long, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration
    );
}

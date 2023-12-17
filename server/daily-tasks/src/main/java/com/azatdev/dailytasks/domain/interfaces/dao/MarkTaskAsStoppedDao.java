package com.azatdev.dailytasks.domain.interfaces.dao;

import java.time.ZonedDateTime;
import java.util.Optional;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;

@FunctionalInterface
public interface MarkTaskAsStoppedDao {

    Void execute(
        long taskId,
        ZonedDateTime finishedAt,
        Optional<Transaction> transaction
    ) throws TaskNotFoundException;
}

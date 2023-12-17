package com.azatdev.dailytasks.domain.interfaces.dao;

import java.time.ZonedDateTime;
import java.util.Optional;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;

@FunctionalInterface
public interface StopActivitySessionDao {

    void execute(
        long activitySessionId,
        ZonedDateTime finishedAt,
        Optional<Transaction> transaction
    );
}

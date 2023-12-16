package com.azatdev.dailytasks.domain.interfaces.dao;

import java.util.Optional;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.ActivitySession;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

@FunctionalInterface
public interface AddNewActivitySessionDao {
    ActivitySession execute(
        NewActivitySession newActivitySession,
        Optional<Transaction> transaction
    );
}

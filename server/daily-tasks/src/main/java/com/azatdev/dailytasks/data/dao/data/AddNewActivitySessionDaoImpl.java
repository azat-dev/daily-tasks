package com.azatdev.dailytasks.data.dao.data;

import java.util.Optional;

import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.ActivitySession;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

public final class AddNewActivitySessionDaoImpl implements AddNewActivitySessionDao {

    private JpaActivitySessionsRepository activitySessionsRepository;
    private MapNewActivitySessionToData mapActivitySessionToData;
    private MapActivitySessionToDomain mapActivitySessionToDomain;

    public AddNewActivitySessionDaoImpl(
        JpaActivitySessionsRepository activitySessionsRepository,
        MapNewActivitySessionToData mapActivitySessionToData,
        MapActivitySessionToDomain mapActivitySessionToDomain
    ) {
        this.activitySessionsRepository = activitySessionsRepository;
        this.mapActivitySessionToData = mapActivitySessionToData;
        this.mapActivitySessionToDomain = mapActivitySessionToDomain;
    }

    @Override
    public ActivitySession execute(
        NewActivitySession newActivitySession,
        Optional<Transaction> transaction
    ) {

        final var newActivitySessionData = mapActivitySessionToData.map(newActivitySession);

        final var savedActivitySessionData = activitySessionsRepository.saveAndFlush(newActivitySessionData);

        return mapActivitySessionToDomain.map(savedActivitySessionData);
    }
}

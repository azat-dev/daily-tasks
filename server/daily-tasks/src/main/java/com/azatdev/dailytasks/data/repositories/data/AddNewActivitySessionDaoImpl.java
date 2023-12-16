package com.azatdev.dailytasks.data.repositories.data;

import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
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
    public ActivitySession execute(NewActivitySession newActivitySession) {

        final var newActivitySessionData = mapActivitySessionToData.map(newActivitySession);

        final var savedActivitySessionData = activitySessionsRepository.saveAndFlush(newActivitySessionData);

        return mapActivitySessionToDomain.map(savedActivitySessionData);
    }
}

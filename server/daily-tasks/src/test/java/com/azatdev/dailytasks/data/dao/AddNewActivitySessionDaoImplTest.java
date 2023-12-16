package com.azatdev.dailytasks.data.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.data.repositories.data.MapActivitySessionToDomain;
import com.azatdev.dailytasks.data.repositories.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.models.ActivitySession;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

interface MapNewActivitySessionToData {
    ActivitySessionData map(NewActivitySession activitySession);
}

final class AddNewActivitySessionDaoImpl implements AddNewActivitySessionDao {

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
}

public class AddNewActivitySessionDaoImplTest {

    private record SUT(
        AddNewActivitySessionDao dao,
        JpaActivitySessionsRepository repository,
        MapNewActivitySessionToData mapNewSessionToData,
        ActivitySessionData sessionMappedToData,
        MapActivitySessionToDomain mapActivitySessionToDomain,
        ActivitySession sessionMappedToDomain
    ) {
    }

    private SUT createSUT() {
        final var repository = mock(JpaActivitySessionsRepository.class);

        final var sessionMappedToData = mock(ActivitySessionData.class);
        final var sessionMappedToDomain = mock(ActivitySession.class);

        final var mapNewSessionToData = mock(MapNewActivitySessionToData.class);
        final var mapActivitySessionToDomain = mock(MapActivitySessionToDomain.class);

        given(mapNewSessionToData.map(any())).willReturn(sessionMappedToData);

        return new SUT(
            new AddNewActivitySessionDaoImpl(
                repository,
                mapNewSessionToData,
                mapActivitySessionToDomain
            ),
            repository,
            mapNewSessionToData,
            sessionMappedToData,
            mapActivitySessionToDomain,
            sessionMappedToDomain
        );
    }

    UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenTaskExistsAndNotStarted_thenCreateNewActivitySession() {

        // Given
        final var newActivitySession = mock(NewActivitySession.class);

        final var sut = createSUT();

        given(sut.repository.saveAndFlush(any())).willReturn(Optional.of(sut.sessionMappedToData));

        // When
        final var createdActivitySession = sut.dao.execute(newActivitySession);

        // Then
        then(sut.repository).should(times(1))
            .saveAndFlush(sut.sessionMappedToData);

        assertThat(createdActivitySession).isEqualTo(sut.sessionMappedToDomain);
    }
}

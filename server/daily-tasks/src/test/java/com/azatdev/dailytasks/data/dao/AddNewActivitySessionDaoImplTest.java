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

import com.azatdev.dailytasks.data.dao.data.AddNewActivitySessionDaoImpl;
import com.azatdev.dailytasks.data.dao.data.MapActivitySessionToDomain;
import com.azatdev.dailytasks.data.dao.data.MapNewActivitySessionToData;
import com.azatdev.dailytasks.data.dao.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.models.ActivitySession;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

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
        final var mapNewSessionToData = mock(MapNewActivitySessionToData.class);
        given(mapNewSessionToData.map(any())).willReturn(sessionMappedToData);

        final var sessionMappedToDomain = mock(ActivitySession.class);
        final var mapActivitySessionToDomain = mock(MapActivitySessionToDomain.class);
        given(mapActivitySessionToDomain.map(any())).willReturn(sessionMappedToDomain);

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

        given(sut.repository.saveAndFlush(any())).willReturn(sut.sessionMappedToData);

        // When
        final var createdActivitySession = sut.dao.execute(
            newActivitySession,
            Optional.empty()
        );

        // Then
        then(sut.repository).should(times(1))
            .saveAndFlush(sut.sessionMappedToData);

        assertThat(createdActivitySession).isEqualTo(sut.sessionMappedToDomain);
    }
}

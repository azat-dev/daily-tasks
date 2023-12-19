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

import com.azatdev.dailytasks.data.dao.activitysession.GetRunningActivitySessionForTaskDaoImpl;
import com.azatdev.dailytasks.data.dao.activitysession.MapActivitySessionToDomain;
import com.azatdev.dailytasks.data.entities.ActivitySessionData;
import com.azatdev.dailytasks.data.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.models.ActivitySession;

class GetRunningActivitySessionForTaskDaoImplTest {

    private record SUT(
        GetRunningActivitySessionForTaskDao dao,
        JpaActivitySessionsRepository repository,
        MapActivitySessionToDomain mapper,
        ActivitySession mappedActivitySession
    ) {
    }

    private SUT createSUT() {
        final var repository = mock(JpaActivitySessionsRepository.class);
        final var mapper = mock(MapActivitySessionToDomain.class);

        final var mappedActivitySession = mock(ActivitySession.class);
        given(mapper.map(any())).willReturn(mappedActivitySession);

        return new SUT(
            new GetRunningActivitySessionForTaskDaoImpl(
                repository,
                mapper
            ),
            repository,
            mapper,
            mappedActivitySession
        );
    }

    @Test
    void execute_givenExistingSession_thenMapAndReturnIt() {

        // Given
        final var sut = createSUT();

        final var userId = UUID.randomUUID();
        final var taskId = 1L;
        final var existingActivitySession = mock(ActivitySessionData.class);

        given(
            sut.repository.findByOwnerIdAndTaskIdAndFinishedAt(
                userId,
                taskId,
                Optional.empty()
            )
        ).willReturn(Optional.of(existingActivitySession));

        // When
        final var foundActivitySession = sut.dao.execute(
            userId,
            taskId
        );

        // Then
        then(sut.repository).should(times(1))
            .findByOwnerIdAndTaskIdAndFinishedAt(
                userId,
                taskId,
                Optional.empty()
            );

        then(sut.mapper).should(times(1))
            .map(existingActivitySession);

        assertThat(foundActivitySession).isNotEmpty();
        assertThat(foundActivitySession.get()).isSameAs(sut.mappedActivitySession);
    }

    @Test
    void execute_givenEmptyDb_thenReturnEmptyOmpitional() {

        // Given
        final var sut = createSUT();

        final var userId = UUID.randomUUID();
        final var taskId = 1L;

        given(
            sut.repository.findByOwnerIdAndTaskIdAndFinishedAt(
                userId,
                taskId,
                Optional.empty()
            )
        ).willReturn(Optional.empty());

        // When
        final var foundActivitySession = sut.dao.execute(
            userId,
            taskId
        );

        // Then
        then(sut.repository).should(times(1))
            .findByOwnerIdAndTaskIdAndFinishedAt(
                userId,
                taskId,
                Optional.empty()
            );

        assertThat(foundActivitySession).isEmpty();
    }
}

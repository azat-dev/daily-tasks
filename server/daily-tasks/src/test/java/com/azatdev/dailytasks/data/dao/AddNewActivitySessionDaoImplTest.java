package com.azatdev.dailytasks.data.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.models.ActivitySession;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Entity
@Table(name = "activity_sessions")
class ActivitySessionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private @Nonnull UserData owner;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private @Nonnull TaskData task;

    private @Nonnull ZonedDateTime startedAt;

    private ZonedDateTime finishedAt;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    public ActivitySessionData(
        @Nonnull UserData owner,
        @Nonnull TaskData task,
        @Nonnull ZonedDateTime startedAt,
        ZonedDateTime finishedAt
    ) {
        this.owner = owner;
        this.task = task;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }
}

interface JpaActivitySessionsRepository extends JpaRepository<ActivitySessionData, Long> {

    Optional<UserData> findByOwneIdAndTaskIdAndFinishedAt(
        UUID ownerId,
        long taskId,
        Optional<ZonedDateTime> finishedAt
    );
}

@FunctionalInterface
interface MapActivitySessionToDomain {
    ActivitySession map(ActivitySessionData data);
}

final class AddNewActivitySessionDaoImpl implements AddNewActivitySessionDao {

    private JpaActivitySessionsRepository repository;
    private MapActivitySessionToDomain mapper;

    public AddNewActivitySessionDaoImpl(
        JpaActivitySessionsRepository repository,
        MapActivitySessionToDomain mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ActivitySession execute(
        UUID userId,
        long taskId,
        ZonedDateTime startedAt,
        Optional<ZonedDateTime> finishedAt
    ) {
        throw new UnsupportedOperationException();
    }
}

class AddNewActivitySessionDaoImplTest {

    private record SUT(
        AddNewActivitySessionDao dao,
        JpaActivitySessionsRepository repository,
        MapActivitySessionToDomain mapper,
        ActivitySession mappedActivitySession
    ) {
    }

    private SUT createSUT() {
        final var repository = mock(JpaActivitySessionsRepository.class);
        final var mapper = mock(MapActivitySessionToDomain.class);
        final var mappedActivitySession = mock(ActivitySession.class);

        return new SUT(
            new AddNewActivitySessionDaoImpl(
                repository,
                mapper
            ),
            repository,
            mapper,
            mappedActivitySession
        );
    }

    @Test
    void execute_givenEmptyDb_thenCreateNewActivitySession() {

        // Given
        final var sut = createSUT();

        final var userId = UUID.randomUUID();
        final var taskId = 1L;
        final var startedAt = ZonedDateTime.now();
        final var finishedAt = ZonedDateTime.now()
            .plusDays(1);

        final var existingActivitySession = mock(ActivitySessionData.class);

        // When
        final var createdActivitySession = sut.dao.execute(
            userId,
            taskId,
            startedAt,
            Optional.of(finishedAt)
        );

        // Then
        then(sut.repository).should(times(1))
            .findByOwneIdAndTaskIdAndFinishedAt(
                userId,
                taskId,
                Optional.empty()
            );

        then(sut.mapper).should(times(1))
            .map(existingActivitySession);

        assertThat(createdActivitySession).isSameAs(sut.mappedActivitySession);
    }
}

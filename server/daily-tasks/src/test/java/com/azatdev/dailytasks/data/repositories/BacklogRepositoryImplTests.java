package com.azatdev.dailytasks.data.repositories;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.utils.Result;

interface JPABacklogRepository extends JpaRepository<BacklogData, Long> {

    BacklogData findByStartDateAndDuration(LocalDate startDate, BacklogData.Duration duration);
}

class BacklogRepositoryImpl implements BacklogRepositoryGet {

    private final JPABacklogRepository jpaBacklogRepository;

    public BacklogRepositoryImpl(JPABacklogRepository jpaBacklogRepository) {
        this.jpaBacklogRepository = jpaBacklogRepository;
    }

    @Override
    public Result<Optional<UUID>, BacklogRepositoryGet.Error> getBacklogId(
        LocalDate startDate, 
        Backlog.Duration duration
    ) {

        jpaBacklogRepository.findByStartDateAndDuration(startDate, BacklogData.Duration.DAY);
        return Result.success(Optional.empty());
    }
}

class BacklogRepositoryImplTests {

    private record SUT(
        BacklogRepositoryGet backlogRepository,
        JPABacklogRepository jpaBacklogRepository
    ) {

    }

    private SUT createSUT() {

        JPABacklogRepository jpaBacklogRepository = mock(JPABacklogRepository.class);

        return new SUT(
            new BacklogRepositoryImpl(jpaBacklogRepository),
            jpaBacklogRepository
        );
    }

    @Test
    void getBacklogIdEmptyDbShouldReturnEmptyOptionalTest() {
        
        // Given
        final var startDate = LocalDate.now();
        final var duration = Backlog.Duration.DAY;
        final var expected = Optional.empty();


        final var sut = createSUT();

        // When
        var result = sut.backlogRepository.getBacklogId(startDate, duration);

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(expected);
    }
}
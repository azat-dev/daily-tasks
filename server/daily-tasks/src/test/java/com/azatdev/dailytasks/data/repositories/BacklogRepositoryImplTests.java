package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.backlog.BacklogRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPABacklogRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.models.Backlog;

@ExtendWith(MockitoExtension.class)
class BacklogRepositoryImplTests {

    @Mock
    JPABacklogRepository jpaBacklogRepository;

    @Mock
    JpaUsersRepository jpaUsersRepository;

    @InjectMocks
    BacklogRepositoryImpl backlogRepository;

    UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void getBacklogId_givenEmptyDb_thenShouldReturnEmptyOptional() {

        // Given
        final var userId = anyUserId();

        final var startDate = LocalDate.now();
        final var duration = Backlog.Duration.DAY;
        final BacklogData expectedBacklogData = null;


        given(
            jpaBacklogRepository.findByOwnerIdAndStartDateAndDuration(
                eq(userId),
                any(LocalDate.class),
                any(BacklogData.Duration.class)
            )
        ).willReturn(expectedBacklogData);

        // When
        final var result = backlogRepository.getBacklogId(
            userId,
            startDate,
            duration
        );

        // Then
        assertThat(result).isEmpty();

        then(jpaBacklogRepository).should(times(1))
            .findByOwnerIdAndStartDateAndDuration(
                userId,
                startDate,
                BacklogData.Duration.DAY
            );
    }

    @Test
    void create_givenEmptyDb_thenShouldCreateBacklog() throws Exception {

        // Given
        final var ownerId = anyUserId();
        
        final var startDate = LocalDate.now();
        final var duration = Backlog.Duration.DAY;
        final var backlogId = 1L;
        
        final var ownerReference = mock(UserData.class);
        given(ownerReference.id()).willReturn(ownerId);

        final var expectedBacklogData = mock(BacklogData.class);
        given(expectedBacklogData.getId()).willReturn(backlogId);

        given(jpaUsersRepository.getReferenceById(ownerId)).willReturn(ownerReference);

        given(jpaUsersRepository.saveAndFlush(any())).willReturn(expectedBacklogData);

        // When

        final var createdBacklogId = backlogRepository.create(
            ownerId,
            startDate,
            duration,
            Optional.empty()
        );

        // Then
        then(jpaBacklogRepository).should(times(1))
            .saveAndFlush(
                new BacklogData(
                    ownerReference,
                    startDate,
                    BacklogData.Duration.DAY
                )
            );

        assertThat(createdBacklogId).isEqualTo(expectedBacklogData.getId());

        then(jpaBacklogRepository).should(times(1))
            .save(any(BacklogData.class));
    }
}

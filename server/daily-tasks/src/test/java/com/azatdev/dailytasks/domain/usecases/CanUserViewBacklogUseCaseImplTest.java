package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.exceptions.BacklogNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetBacklogByIdDao;
import com.azatdev.dailytasks.domain.models.Backlog;

class CanUserViewBacklogUseCaseImplTest {

    private record SUT(
        CanUserViewBacklogUseCase useCase,
        GetBacklogByIdDao getBacklogByIdDao
    ) {
    }

    private SUT createSUT() {

        final var getBacklogByIdDao = mock(GetBacklogByIdDao.class);
        final var useCase = new CanUserViewBacklogUseCaseImpl(getBacklogByIdDao);

        return new SUT(
            useCase,
            getBacklogByIdDao
        );
    }

    private UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenBacklogDoesntExist_thenThrowBacklogNotFoundException() {

        // Given
        final var sut = createSUT();

        final var userId = anyUserId();
        final var backlogId = 1L;

        given(sut.getBacklogByIdDao.execute(backlogId)).willReturn(Optional.empty());

        // When
        final var exception = assertThrows(
            BacklogNotFoundException.class,
            () -> sut.useCase.execute(
                userId,
                backlogId
            )
        );

        // Then
        assertThat(exception.getBacklogId()).isEqualTo(backlogId);
    }

    private void test_execution(
        UUID userId,
        UUID ownerId,
        boolean expectedResult
    ) throws Exception {

        // Given
        final var sut = createSUT();

        final var backlogId = 1L;

        final var backlog = mock(Backlog.class);

        given(backlog.ownerId()).willReturn(ownerId);

        given(sut.getBacklogByIdDao.execute(backlogId)).willReturn(Optional.of(backlog));

        // When
        final var result = sut.useCase.execute(
            userId,
            backlogId
        );

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void execute_givenUserIsOwnerOfBacklog_thenReturnTrue() throws Exception {

        final var userId = anyUserId();
        final var ownerId = userId;

        test_execution(
            userId,
            ownerId,
            true
        );
    }

    @Test
    void execute_givenUserIsNotOwnerOfBacklog_thenReturnFalse() throws Exception {

        final var userId = anyUserId();
        final var ownerId = anyUserId();

        test_execution(
            userId,
            ownerId,
            false
        );
    }
}

package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;
import com.azatdev.dailytasks.domain.models.Task;

class IsUserOwnerOfTaskUseCaseTest {

    private record SUT(
        IsUserOwnerOfTaskUseCase useCase,
        GetTaskDao taskDao
    ) {
    }

    private SUT createSUT() {

        final var getTaskDao = mock(GetTaskDao.class);
        final var useCase = new IsUserOwnerOfTaskUseCase(getTaskDao);

        return new SUT(
            useCase,
            getTaskDao
        );
    }

    private UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenTaskDoesntExist_thenThrowTaskNotFoundException() {

        // Given
        final var sut = createSUT();

        final var userId = anyUserId();
        final var taskId = 1L;

        given(sut.taskDao.execute(taskId)).willReturn(Optional.empty());

        // When
        final var exception = assertThrows(
            TaskNotFoundException.class,
            () -> sut.useCase.execute(
                userId,
                taskId
            )
        );

        // Then
        assertThat(exception.getTaskId()).isEqualTo(taskId);
    }

    private void test_execution(
        UUID userId,
        UUID ownerId,
        boolean expectedResult
    ) throws Exception {

        // Given
        final var sut = createSUT();

        final var taskId = 1L;

        final var taskData = mock(Task.class);

        given(taskData.ownerId()).willReturn(ownerId);

        given(sut.taskDao.execute(taskId)).willReturn(Optional.of(taskData));

        // When
        final var result = sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void execute_givenUserIsOwnerOfTask_thenReturnTrue() throws Exception {

        final var userId = anyUserId();
        final var ownerId = userId;

        test_execution(
            userId,
            ownerId,
            true
        );
    }

    @Test
    void execute_givenUserIsNotOwnerOfTask_thenReturnFalse() throws Exception {

        final var userId = anyUserId();
        final var ownerId = anyUserId();

        test_execution(
            userId,
            ownerId,
            false
        );
    }
}

package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;

@FunctionalInterface
interface DeleteTaskUseCase {
    void execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException;
}

@FunctionalInterface
interface DeleteTaskDao {
    void execute(long taskId) throws TaskNotFoundException;
}

@FunctionalInterface
interface DoesUserHavePermissionToDeleteTaskUseCase {
    boolean execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException, AccessDeniedException;
}

final class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {

    private final DoesUserHavePermissionToDeleteTaskUseCase doesUserHavePermissionToDeleteTaskUseCase;
    private final StopTaskUseCase stopTaskUseCase;
    private final DeleteTaskDao deleteTaskDao;
    private final TransactionFactory transactionFactory;

    public DeleteTaskUseCaseImpl(
        DoesUserHavePermissionToDeleteTaskUseCase doesUserHavePermissionToDeleteTaskUseCase,
        StopTaskUseCase stopTaskUseCase,
        DeleteTaskDao deleteTaskDao,
        TransactionFactory transactionFactory
    ) {
        this.doesUserHavePermissionToDeleteTaskUseCase = doesUserHavePermissionToDeleteTaskUseCase;
        this.stopTaskUseCase = stopTaskUseCase;
        this.deleteTaskDao = deleteTaskDao;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException, AccessDeniedException {

        final boolean hasPermission = doesUserHavePermissionToDeleteTaskUseCase.execute(
            userId,
            taskId
        );

        if (!hasPermission) {
            throw new AccessDeniedException(
                userId,
                "deleteTask",
                String.valueOf(taskId)
            );
        }
        return;

        // stopTaskUseCase.execute(
        // taskId
        // );

        // deleteTaskDao.execute(
        // taskId
        // );
    }
}

class DeleteTaskUseCaseImplTest {

    private record SUT(
        DeleteTaskUseCase useCase,
        DoesUserHavePermissionToDeleteTaskUseCase doesUserHavePermissionToDeleteTaskUseCase,
        StopTaskUseCase stopTaskUseCase,
        DeleteTaskDao deleteTaskDao,
        TransactionFactory transactionFactory,
        Transaction transaction
    ) {
    }

    private SUT createSUT() {

        final var doesUserHavePermissionToDeleteTaskUseCase = mock(DoesUserHavePermissionToDeleteTaskUseCase.class);
        final var stopTaskUseCase = mock(StopTaskUseCase.class);
        final var deleteTaskDao = mock(DeleteTaskDao.class);

        final var transaction = mock(Transaction.class);
        final var transactionFactory = mock(TransactionFactory.class);

        final var useCase = new DeleteTaskUseCaseImpl(
            doesUserHavePermissionToDeleteTaskUseCase,
            stopTaskUseCase,
            deleteTaskDao,
            transactionFactory
        );

        return new SUT(
            useCase,
            doesUserHavePermissionToDeleteTaskUseCase,
            stopTaskUseCase,
            deleteTaskDao,
            transactionFactory,
            transaction
        );
    }

    UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenUserDoesntHavePermissionToDelete_thenThrowAccessDeniedException() {

        // Given
        final var userId = anyUserId();
        final var taskId = 1L;

        final var sut = createSUT();

        given(
            sut.doesUserHavePermissionToDeleteTaskUseCase.execute(
                any(),
                anyLong()
            )
        ).willReturn(false);

        // When
        final var exception = assertThrows(
            AccessDeniedException.class,
            () -> sut.useCase.execute(
                userId,
                taskId
            )
        );

        // Then
        then(sut.doesUserHavePermissionToDeleteTaskUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );

        assertThat(exception.getOperation()).isEqualTo("deleteTask");
        assertThat(exception.getResource()).isEqualTo(String.valueOf(taskId));
        assertThat(exception.getUserId()).isEqualTo(userId);
    }
}

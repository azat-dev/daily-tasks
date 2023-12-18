package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
interface CanUserDeleteTaskUseCase {
    boolean execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException, AccessDeniedException;
}

final class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {

    private final CanUserDeleteTaskUseCase canUserDeleteTaskUseCase;
    private final StopTaskUseCase stopTaskUseCase;
    private final DeleteTaskDao deleteTaskDao;
    private final TransactionFactory transactionFactory;

    public DeleteTaskUseCaseImpl(
        CanUserDeleteTaskUseCase canUserDeleteTaskUseCase,
        StopTaskUseCase stopTaskUseCase,
        DeleteTaskDao deleteTaskDao,
        TransactionFactory transactionFactory
    ) {
        this.canUserDeleteTaskUseCase = canUserDeleteTaskUseCase;
        this.stopTaskUseCase = stopTaskUseCase;
        this.deleteTaskDao = deleteTaskDao;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException, AccessDeniedException {

        final boolean hasPermission = canUserDeleteTaskUseCase.execute(
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

        stopTaskUseCase.execute(
            null,
            taskId
        );

        deleteTaskDao.execute(taskId);
    }
}

class DeleteTaskUseCaseImplTest {

    private record SUT(
        DeleteTaskUseCase useCase,
        CanUserDeleteTaskUseCase canUserDeleteTaskUseCase,
        StopTaskUseCase stopTaskUseCase,
        DeleteTaskDao deleteTaskDao,
        TransactionFactory transactionFactory,
        Transaction transaction
    ) {
    }

    private SUT createSUT() {

        final var canUserDeleteTaskUseCase = mock(CanUserDeleteTaskUseCase.class);
        final var stopTaskUseCase = mock(StopTaskUseCase.class);
        final var deleteTaskDao = mock(DeleteTaskDao.class);

        final var transaction = mock(Transaction.class);
        final var transactionFactory = mock(TransactionFactory.class);

        final var useCase = new DeleteTaskUseCaseImpl(
            canUserDeleteTaskUseCase,
            stopTaskUseCase,
            deleteTaskDao,
            transactionFactory
        );

        return new SUT(
            useCase,
            canUserDeleteTaskUseCase,
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
            sut.canUserDeleteTaskUseCase.execute(
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
        then(sut.canUserDeleteTaskUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );

        assertThat(exception.getOperation()).isEqualTo("deleteTask");
        assertThat(exception.getResource()).isEqualTo(String.valueOf(taskId));
        assertThat(exception.getUserId()).isEqualTo(userId);
    }

    @Test
    void execute_givenUserHasPermissionToDelete_thenStopTaskAndThenDelete() {

        // Given
        final var userId = anyUserId();
        final var taskId = 1L;

        final var sut = createSUT();

        given(
            sut.canUserDeleteTaskUseCase.execute(
                any(),
                anyLong()
            )
        ).willReturn(true);

        willThrow(new RuntimeException()).given(sut.deleteTaskDao);

        // When
        sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        then(sut.transaction).should(times(1))
            .begin();
        then(sut.transaction).should(never())
            .rollback();
        then(sut.transaction).should(times(1))
            .commit();

        then(sut.canUserDeleteTaskUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );

        then(sut.stopTaskUseCase).should(times(1))
            .execute(
                null,
                taskId
            );

        then(sut.deleteTaskDao).should(times(1))
            .execute(taskId);
    }

    @Test
    void execute_givenUserHasPermissionToDelete_whenExceptionDuringTransaction_thenRollbackThrowAnError() {

        // Given
        final var userId = anyUserId();
        final var taskId = 1L;

        final var sut = createSUT();

        given(
            sut.canUserDeleteTaskUseCase.execute(
                any(),
                anyLong()
            )
        ).willReturn(true);

        // When
        sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        then(sut.transaction).should(times(1))
            .begin();
        then(sut.transaction).should(times(1))
            .rollback();
        then(sut.transaction).should(never())
            .commit();

        then(sut.canUserDeleteTaskUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );
    }
}

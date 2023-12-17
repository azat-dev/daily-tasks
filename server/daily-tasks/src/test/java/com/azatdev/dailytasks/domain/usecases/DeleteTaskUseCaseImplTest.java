package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
interface DeleteTaskUseCase {
    void execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException;
}

@FunctionalInterface
interface StopTaskUseCase {
    void execute(long taskId) throws TaskNotFoundException;
}

@FunctionalInterface
interface DeleteTaskDao {
    void execute(long taskId) throws TaskNotFoundException;
}

@FunctionalInterface
interface DoesUserHavePermissionToDeleteTaskUseCase {
    boolean execute(
        UUID userId,
        Task task
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
    ) throws TaskNotFoundException {

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

        // When
        final var exception = assertThrows(
            AccessDeniedException.class,
            () -> sut.useCase.execute(
                userId,
                taskId
            )
        );

        // Then
        assertThat(exception.getOperation()).isEqualTo("deleteTask");
        assertThat(exception.getResource()).isEqualTo(String.valueOf(taskId));
        assertThat(exception.getUserId()).isEqualTo(userId);
    }
}

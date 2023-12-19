package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.models.Task;

record EditTaskData(
    Optional<String> title,
    Optional<Task.Priority> priority,
    Optional<String> description
) {
}

@FunctionalInterface
interface EditTaskUseCase {

    void execute(
        UUID userId,
        long taskId,
        EditTaskData data
    ) throws AccessDeniedException, TaskNotFoundException;
}

@FunctionalInterface
interface CanUserEditTaskUseCase {

    boolean execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException;
}

@FunctionalInterface
interface UpdateTaskDao {

    void execute(
        long taskId,
        EditTaskData data
    ) throws TaskNotFoundException;
}

final class EditTaskUseCaseImpl implements EditTaskUseCase {

    private final CanUserEditTaskUseCase canUserEditTaskUseCase;
    private final UpdateTaskDao updateTaskDao;

    public EditTaskUseCaseImpl(
        CanUserEditTaskUseCase canUserEditTaskUseCase,
        UpdateTaskDao updateTaskDao
    ) {
        this.canUserEditTaskUseCase = canUserEditTaskUseCase;
        this.updateTaskDao = updateTaskDao;
    }

    @Override
    public void execute(
        UUID userId,
        long taskId,
        EditTaskData data
    ) throws AccessDeniedException, TaskNotFoundException {

        final var canUserEdit = canUserEditTaskUseCase.execute(
            userId,
            taskId
        );

        if (!canUserEdit) {
            throw new AccessDeniedException(
                userId,
                "editTask",
                String.valueOf(taskId)
            );
        }

        throw new RuntimeException("Not implemented");
    }
}

class EditTaskUseCaseImplTest {

    private record SUT(
        EditTaskUseCaseImpl useCase,
        CanUserEditTaskUseCase canUserEditTaskUseCase,
        UpdateTaskDao updateTaskDao
    ) {
    }

    private SUT createSUT() {
        final var canUserEditTaskUseCase = mock(CanUserEditTaskUseCase.class);
        final var updateTaskDao = mock(UpdateTaskDao.class);

        final var useCase = new EditTaskUseCaseImpl(
            canUserEditTaskUseCase,
            updateTaskDao
        );

        return new SUT(
            useCase,
            canUserEditTaskUseCase,
            updateTaskDao
        );
    }

    private UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenUserCanNotEditTask_thenThrowException() throws Exception {

        // Given
        final var userId = anyUserId();
        final var taskId = 111L;
        final var data = new EditTaskData(
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );
        final var sut = createSUT();

        given(
            sut.canUserEditTaskUseCase.execute(
                any(),
                anyLong()
            )
        ).willReturn(false);

        // When
        final var exception = catchThrowableOfType(
            () -> sut.useCase.execute(
                userId,
                taskId,
                data
            ),
            AccessDeniedException.class
        );

        // Then

        then(sut.canUserEditTaskUseCase).should((times(1)))
            .execute(
                userId,
                taskId
            );

        then(sut.updateTaskDao).shouldHaveNoInteractions();

        assertThat(exception.getUserId()).isEqualTo(userId);
        assertThat(exception.getOperation()).isEqualTo("editTask");
        assertThat(exception.getResource()).isEqualTo(String.valueOf(taskId));
    }
}

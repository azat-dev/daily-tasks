package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

interface AddTaskInBacklogUseCase {

    enum Error {
        INTERNAL_ERROR
    }

    Result<Task, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration
    );
}

interface AddBacklogForDateIfDoesntExistUseCase {

    enum Error {
        INTERNAL_ERROR
    }

    /**
     * Adds backlog for given date and duration if it does not exist.
     * 
     * @param date
     * @param backlogDuration
     * @return Backlog Id
     */
    Result<Long, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration
    );
}

class AddTaskInBacklogUseCaseImpl implements AddTaskInBacklogUseCase {

    private final AddBacklogForDateIfDoesntExistUseCase addBacklogUseCase;

    public AddTaskInBacklogUseCaseImpl(
        AddBacklogForDateIfDoesntExistUseCase addBacklogUseCase
    ) {
        this.addBacklogUseCase = addBacklogUseCase;
    }

    @Override
    public Result<Task, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration
    ) {

        final var backlogIdResult = addBacklogUseCase.execute(
            date,
            backlogDuration
        );

        if (!backlogIdResult.isSuccess()) {
            return Result.failure(Error.INTERNAL_ERROR);
        }

        final var backlogId = backlogIdResult.getValue();

        return null;
    }
}

class AddTaskInBacklogUseCaseImplTests {

    private record SUT(
        AddTaskInBacklogUseCase useCase,
        AddBacklogForDateIfDoesntExistUseCase addBacklogUseCase
    ) {
    }

    private SUT createSUT() {
        final var addBacklogUseCase = mock(AddBacklogForDateIfDoesntExistUseCase.class);

        return new SUT(
            new AddTaskInBacklogUseCaseImpl(addBacklogUseCase),
            addBacklogUseCase
        );
    }

    @Test
    void executeShouldAddBacklogAndReturnTaskTest() {

        // Given
        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();

        final var backlogId = 1L;

        final var sut = createSUT();

        given(sut.addBacklogUseCase.execute(backlogStartDate, backlogDuration))
            .willReturn(Result.success(backlogId));

        // When
        final var result = sut.useCase.execute(
            backlogStartDate,
            backlogDuration
        );

        // Then
        
        then(sut.addBacklogUseCase).should(times(1))
            .execute(
                backlogStartDate,
                backlogDuration
            );

        assertThat(result).isNotNull();
    }
}

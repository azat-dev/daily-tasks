package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;

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

class AddTaskInBacklogUseCaseImpl implements AddTaskInBacklogUseCase {

    @Override
    public Result<Task, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration
    ) {
        return null;
    }
}

class AddTaskInBacklogUseCaseImplTests {

    private record SUT(AddTaskInBacklogUseCase useCase) {
    }

    private SUT createSUT() {
        return new SUT(new AddTaskInBacklogUseCaseImpl());
    }

    @Test
    void executeShouldAddBacklogAndReturnTaskTest() {

        // Given
        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();

        final var sut = createSUT();

        // When
        final var result = sut.useCase.execute(
            backlogStartDate,
            backlogDuration
        );

        // Then
        assertThat(result).isNotNull();
    }
}

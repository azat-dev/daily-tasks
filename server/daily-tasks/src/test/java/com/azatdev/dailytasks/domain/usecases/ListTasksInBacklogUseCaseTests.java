package com.azatdev.dailytasks.domain.usecases;

import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito.*;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@FunctionalInterface
interface AdjustDateToStartOfBacklog {
    LocalDate calculateAdjustedDate(LocalDate date, Backlog.Duration duration);
}

interface BacklogRepositoryGet {

    UUID getBacklogId(LocalDate startDate, Backlog.Duration duration);
}

interface TasksRepositoryList {

    Task[] list(UUID backlogId);
}

class Result<Value, Error> {

    private final Value value;
    private final Error error;
    private final boolean isSuccess;

    private Result(Value value, Error error, boolean isSuccess) {
        this.value = value;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    public static <Value, Error> Result<Value, Error> success(Value value) {
        return new Result<>(value, null, true);
    }

    public static <Value, Error> Result<Value, Error> failure(Error error) {
        return new Result<>(null, error, false);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Value getValue() throws IllegalStateException {
        if (!isSuccess) {
            throw new IllegalStateException("Result does not contain a value");
        }
        return value;
    }

    public Error getError() throws IllegalStateException {
        if (isSuccess) {
            throw new IllegalStateException("Result does not contain an error");
        }
        return error;
    }
}


interface ListTasksInBacklogUseCase {

    enum Error {
        INTERNAL_ERROR
    }

    Result<Task[], Error> execute(LocalDate date, Backlog.Duration duration);
}

class ListTasksInBacklogUseCaseImpl implements ListTasksInBacklogUseCase {
    
        private final BacklogRepositoryGet backlogRepository;
        private final TasksRepositoryList tasksRepository;
        private final AdjustDateToStartOfBacklog adjustDateToStart;
    
        public ListTasksInBacklogUseCaseImpl(
            BacklogRepositoryGet backlogRepository,
            TasksRepositoryList tasksRepository,
            AdjustDateToStartOfBacklog adjustBacklogTime
        ) {
            this.backlogRepository = backlogRepository;
            this.tasksRepository = tasksRepository;
            this.adjustDateToStart = adjustBacklogTime;
        }
    
        @Override
        public Result<Task[], Error> execute(LocalDate forDate, Backlog.Duration duration) {
            return Result.failure(Error.INTERNAL_ERROR);
            // var startDate = adjustDateToStart.calculateAdjustedDate(forDate, duration);
            // var backlogId = backlogRepository.getBacklogId(startDate, duration);
            // var tasks = tasksRepository.list(backlogId);

            // return Result.success(tasks);
        }
}

public class ListTasksInBacklogUseCaseTests {
    
    private record SUT(
        ListTasksInBacklogUseCase listTasksInBacklogUseCase,
        BacklogRepositoryGet backlogRepository,
        TasksRepositoryList tasksRepository,
        AdjustDateToStartOfBacklog adjustBacklogTime
    ) {

    }

    private SUT createSUT() {
        BacklogRepositoryGet backlogRepository = mock(BacklogRepositoryGet.class);
        TasksRepositoryList tasksRepository = mock(TasksRepositoryList.class);
        AdjustDateToStartOfBacklog adjustBacklogTime = mock(AdjustDateToStartOfBacklog.class);

        var useCase = new ListTasksInBacklogUseCaseImpl(
            backlogRepository,
            tasksRepository,
            adjustBacklogTime
        );

        return new SUT(
            useCase,
            backlogRepository,
            tasksRepository,
            adjustBacklogTime
        );
    }

    @Test
    void executeEmptyDbShouldReturnEmptyListTest() {
        
        // Given
        UUID backlogId = UUID.randomUUID();

        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();
        final Task[] expectedTasks = {};
        final LocalDate adjustedBacklogStartDate = backlogStartDate;

        final var sut = createSUT();

        given(sut.adjustBacklogTime.calculateAdjustedDate(backlogStartDate, backlogDuration))
            .willReturn(adjustedBacklogStartDate);

        given(sut.backlogRepository.getBacklogId(backlogStartDate, backlogDuration))
            .willReturn(backlogId);
    
        given(sut.tasksRepository.list(backlogId))
            .willReturn(expectedTasks);


        // When
        var result = sut.listTasksInBacklogUseCase.execute(backlogStartDate, backlogDuration);

        // Then
        then(sut.backlogRepository)
            .should(times(1))
            .getBacklogId(adjustedBacklogStartDate, backlogDuration);
        
        then(sut.tasksRepository)
            .should(times(1))
            .list(backlogId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(expectedTasks);
    }
}

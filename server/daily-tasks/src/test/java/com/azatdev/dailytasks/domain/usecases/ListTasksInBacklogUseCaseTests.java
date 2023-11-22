package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;


class Result<Value, Failure> {

    private final Value value;
    private final Failure error;
    private final boolean isSuccess;

    private Result(Value value, Failure error, boolean isSuccess) {
        this.value = value;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    public static <Value, Failure> Result<Value, Failure> success(Value value) {
        return new Result<>(value, null, true);
    }

    public static <Value, Failure> Result<Value, Failure> failure(Failure error) {
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

    public Failure getError() throws IllegalStateException {
        if (isSuccess) {
            throw new IllegalStateException("Result does not contain an error");
        }
        return error;
    }
}

@FunctionalInterface
interface AdjustDateToStartOfBacklog {
    LocalDate calculateAdjustedDate(LocalDate date, Backlog.Duration duration);
}

interface BacklogRepositoryGet {

    enum Error {
        INTERNAL_ERROR
    }

    Result<Optional<UUID>, Error> getBacklogId(LocalDate startDate, Backlog.Duration duration);
}

interface TasksRepositoryList {

    enum Error {
        INTERNAL_ERROR
    }

    Result<Task[], Error> list(UUID backlogId);
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
            var backlogStartDate = adjustDateToStart.calculateAdjustedDate(forDate, duration);
            var getBacklogIdResult = backlogRepository.getBacklogId(backlogStartDate, duration);

            if (!getBacklogIdResult.isSuccess()) {
                return Result.success(new Task[]{});
            }

            final var optionalBacklogId = getBacklogIdResult.getValue();

            if (optionalBacklogId.isEmpty()) {
                return Result.success(new Task[]{});
            }

            var listTasksResult = tasksRepository.list(optionalBacklogId.get());

            if (listTasksResult.isSuccess()) {
                return Result.success(listTasksResult.getValue());
            }

            return Result.failure(Error.INTERNAL_ERROR);
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
        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();
        final Task[] expectedTasks = {};
        final LocalDate adjustedBacklogStartDate = backlogStartDate;

        final var sut = createSUT();

        given(sut.adjustBacklogTime.calculateAdjustedDate(backlogStartDate, backlogDuration))
            .willReturn(adjustedBacklogStartDate);

        given(sut.backlogRepository.getBacklogId(backlogStartDate, backlogDuration))
            .willReturn(Result.success(Optional.empty()));
    

        // When
        var result = sut.listTasksInBacklogUseCase.execute(backlogStartDate, backlogDuration);

        // Then
        then(sut.backlogRepository)
            .should(times(1))
            .getBacklogId(adjustedBacklogStartDate, backlogDuration);
        
        then(sut.tasksRepository)
            .should(never())
            .list(any());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(expectedTasks);
    }


    @Test
    void executeEmptyBacklogShouldReturnEmptyListTest() {
        
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
            .willReturn(Result.success(Optional.of(backlogId)));
    
        given(sut.tasksRepository.list(backlogId))
            .willReturn(Result.success(expectedTasks));


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

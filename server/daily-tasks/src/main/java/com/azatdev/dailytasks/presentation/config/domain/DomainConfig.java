package com.azatdev.dailytasks.presentation.config.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.dao.DeleteAllActivitySessionsOfTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.DeleteTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetBacklogByIdDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.MarkTaskAsStoppedDao;
import com.azatdev.dailytasks.domain.interfaces.dao.StopActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.dao.UpdateTaskStatusDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.utils.CurrentTimeProvider;
import com.azatdev.dailytasks.domain.usecases.CanUserDeleteTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.CanUserDeleteTaskUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.CanUserViewBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.CanUserViewBacklogUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.CanUserViewTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.CanUserViewTaskUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.CreateBacklogForDateIfDoesntExistUseCase;
import com.azatdev.dailytasks.domain.usecases.CreateBacklogForDateIfDoesntExistUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.DeleteTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.DeleteTaskUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.GetTaskDetailsUseCase;
import com.azatdev.dailytasks.domain.usecases.GetTaskDetailsUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.SignUpAppUserUseCase;
import com.azatdev.dailytasks.domain.usecases.SignUpAppUserUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.StartTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.StartTaskUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.StopTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.StopTaskUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartDateInBacklogImpl;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

@Configuration
public class DomainConfig {

    @Bean
    AdjustDateToStartOfBacklog adjustDateToStartOfBacklog() {
        return new AdjustDateToStartDateInBacklogImpl();
    }

    @Bean
    public CanUserViewBacklogUseCase canUserViewBacklogUseCase(GetBacklogByIdDao getBacklogByIdDao) {
        return new CanUserViewBacklogUseCaseImpl(getBacklogByIdDao);
    }

    @Bean
    public ListTasksInBacklogUseCase listTasksInBacklogUseCase(
        CanUserViewBacklogUseCase canUserViewBacklogUseCase,
        BacklogRepositoryGet backlogRepository,
        TasksRepositoryList tasksRepository,
        AdjustDateToStartOfBacklog adjustDateToStartOfBacklog
    ) {
        return new ListTasksInBacklogUseCaseImpl(
            canUserViewBacklogUseCase,
            backlogRepository,
            tasksRepository,
            adjustDateToStartOfBacklog
        );
    }

    @Bean
    public CreateBacklogForDateIfDoesntExistUseCase createBacklogForDateIfDoesntExistUseCase(
        AdjustDateToStartOfBacklog adjustDateToStartOfBacklog,
        BacklogRepositoryCreate backlogRepository
    ) {
        return new CreateBacklogForDateIfDoesntExistUseCaseImpl(
            adjustDateToStartOfBacklog,
            backlogRepository
        );
    }

    @Bean
    public CreateTaskInBacklogUseCase createTaskInBacklogUseCase(
        CreateBacklogForDateIfDoesntExistUseCase createBacklogForDateIfDoesntExistUseCase,
        TasksRepositoryCreate tasksRepository,
        TransactionFactory transactionFactory
    ) {

        return new CreateTaskInBacklogUseCaseImpl(
            createBacklogForDateIfDoesntExistUseCase,
            tasksRepository,
            transactionFactory
        );
    }

    @Bean
    public SignUpAppUserUseCase signUpAppUserUseCase(UsersRepositoryCreate usersRepository) {
        return new SignUpAppUserUseCaseImpl(usersRepository);
    }

    @Bean
    public StartTaskUseCase startTaskUseCase(
        CurrentTimeProvider currentTimeProvider,
        GetRunningActivitySessionForTaskDao getRunningActivitySessionForTaskDao,
        AddNewActivitySessionDao addNewActivitySessionDao,
        UpdateTaskStatusDao updateTaskStatusDao,
        TransactionFactory transactionFactory
    ) {
        return new StartTaskUseCaseImpl(
            currentTimeProvider,
            getRunningActivitySessionForTaskDao,
            addNewActivitySessionDao,
            updateTaskStatusDao,
            transactionFactory
        );
    }

    @Bean
    public CanUserViewTaskUseCase canUserViewTaskUseCase(GetTaskDao getTaskDao) {
        return new CanUserViewTaskUseCaseImpl(getTaskDao);
    }

    @Bean
    public GetTaskDetailsUseCase getTaskDetailsUseCase(
        CanUserViewTaskUseCase canUserViewTaskUseCase,
        GetTaskDao getTaskDao
    ) {
        return new GetTaskDetailsUseCaseImpl(
            canUserViewTaskUseCase,
            getTaskDao
        );
    }

    @Bean
    public StopTaskUseCase stopTaskUseCase(
        CurrentTimeProvider currentTimeProvider,
        GetRunningActivitySessionForTaskDao getCurrentRunningActivitySessionForTaskDao,
        StopActivitySessionDao stopActivitySessionDao,
        MarkTaskAsStoppedDao markTaskAsStoppedDao,
        TransactionFactory transactionFactory
    ) {
        return new StopTaskUseCaseImpl(
            currentTimeProvider,
            getCurrentRunningActivitySessionForTaskDao,
            stopActivitySessionDao,
            markTaskAsStoppedDao,
            transactionFactory
        );
    }

    @Bean
    public DeleteTaskUseCase deleteTaskUseCase(
        CanUserDeleteTaskUseCase canUserDeleteTaskUseCase,
        StopTaskUseCase stopTaskUseCase,
        DeleteTaskDao deleteTaskDao,
        DeleteAllActivitySessionsOfTaskDao deleteAllActivitySessionsOfTaskDao,
        TransactionFactory transactionFactory
    ) {
        return new DeleteTaskUseCaseImpl(
            canUserDeleteTaskUseCase,
            stopTaskUseCase,
            deleteTaskDao,
            deleteAllActivitySessionsOfTaskDao,
            transactionFactory
        );
    }

    @Bean
    public CanUserDeleteTaskUseCase canUserDeleteTaskUseCase(GetTaskDao getTaskDao) {
        return new CanUserDeleteTaskUseCaseImpl(getTaskDao);
    }
}

package com.azatdev.dailytasks.presentation.config.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryCreate;
import com.azatdev.dailytasks.domain.usecases.CreateBacklogForDateIfDoesntExistUseCase;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.SignUpAppUserUseCase;
import com.azatdev.dailytasks.domain.usecases.SignUpAppUserUseCaseImpl;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartDateInBacklogImpl;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

@Configuration
public class DomainConfig {

    @Bean
    AdjustDateToStartOfBacklog adjustDateToStartOfBacklog() {
        return new AdjustDateToStartDateInBacklogImpl();
    }

    @Bean
    public ListTasksInBacklogUseCase listTasksInBacklogUseCase(
        BacklogRepositoryGet backlogRepository,
        TasksRepositoryList tasksRepository,
        AdjustDateToStartOfBacklog adjustDateToStartOfBacklog
    ) {
        return new ListTasksInBacklogUseCaseImpl(
            backlogRepository,
            tasksRepository,
            adjustDateToStartOfBacklog
        );
    }

    @Bean
    public CreateBacklogForDateIfDoesntExistUseCase createBacklogForDateIfDoesntExistUseCase() {
        return null;
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
}

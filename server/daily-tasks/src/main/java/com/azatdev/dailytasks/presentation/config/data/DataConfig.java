package com.azatdev.dailytasks.presentation.config.data;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.azatdev.dailytasks.data.repositories.data.AddNewActivitySessionDaoImpl;
import com.azatdev.dailytasks.data.repositories.data.MapActivitySessionToDomain;
import com.azatdev.dailytasks.data.repositories.data.MapActivitySessionToDomainImpl;
import com.azatdev.dailytasks.data.repositories.data.MapNewActivitySessionToData;
import com.azatdev.dailytasks.data.repositories.data.MapNewActivitySessionToDataImpl;
import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomainImpl;
import com.azatdev.dailytasks.data.repositories.data.TasksRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.data.UpdateTaskStatusDaoImpl;
import com.azatdev.dailytasks.data.repositories.data.transaction.TransactionImplFactory;
import com.azatdev.dailytasks.data.repositories.persistence.GetRunningActivitySessionForTaskDaoImpl;
import com.azatdev.dailytasks.data.repositories.persistence.backlog.BacklogRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaBacklogsRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.UpdateTaskStatusDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.interfaces.utils.CurrentTimeProvider;

@Configuration
public class DataConfig {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionFactory transactionFactory() {
        return new TransactionImplFactory(transactionManager);
    }

    @Bean
    public BacklogRepository backlogRepository(
        JpaUsersRepository jpaUsersRepository,
        JpaBacklogsRepository jpaBacklogRepository
    ) {
        return new BacklogRepositoryImpl(
            jpaUsersRepository,
            jpaBacklogRepository
        );
    }

    @Bean
    public TasksRepository tasksRepository(
        JpaUsersRepository jpaUsersRepository,
        JpaBacklogsRepository jpaBacklogsRepository,
        JpaTasksRepository jpaTasksRepository
    ) {

        return new TasksRepositoryImpl(
            jpaUsersRepository::getReferenceById,
            jpaBacklogsRepository::getReferenceById,
            jpaTasksRepository,
            new MapTaskDataToDomainImpl()
        );
    }

    @Bean
    public MapActivitySessionToDomain mapActivitySessionToDomain() {
        return new MapActivitySessionToDomainImpl();
    }

    @Bean
    public MapNewActivitySessionToData mapNewActivitySessionToData(
        JpaUsersRepository jpaUsersRepository,
        JpaTasksRepository jpaTasksRepository
    ) {
        return new MapNewActivitySessionToDataImpl(
            jpaUsersRepository::getReferenceById,
            jpaTasksRepository::getReferenceById
        );
    }

    @Bean
    public GetRunningActivitySessionForTaskDao getRunningActivitySessionForTaskDao(
        JpaActivitySessionsRepository jpaActivitySessionsRepository,
        MapActivitySessionToDomain mapActivitySessionToDomain
    ) {
        return new GetRunningActivitySessionForTaskDaoImpl(
            jpaActivitySessionsRepository,
            mapActivitySessionToDomain
        );
    }

    @Bean
    public CurrentTimeProvider currentTimeProvider() {
        return new CurrentTimeProvider() {
            @Override
            public ZonedDateTime execute() {
                return ZonedDateTime.now();
            }
        };
    }

    @Bean
    public AddNewActivitySessionDao addNewActivitySessionDao(
        JpaActivitySessionsRepository activitySessionsRepository,
        MapNewActivitySessionToData mapActivitySessionToData,
        MapActivitySessionToDomain mapActivitySessionToDomain
    ) {
        return new AddNewActivitySessionDaoImpl(
            activitySessionsRepository,
            mapActivitySessionToData,
            mapActivitySessionToDomain
        );
    }

    @Bean
    public UpdateTaskStatusDao updateTaskStatusDao(JpaTasksRepository jpaTasksRepository) {
        return new UpdateTaskStatusDaoImpl(jpaTasksRepository);
    }
}

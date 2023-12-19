package com.azatdev.dailytasks.presentation.config.data;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.azatdev.dailytasks.data.dao.data.AddNewActivitySessionDaoImpl;
import com.azatdev.dailytasks.data.dao.data.DeleteTaskDaoImpl;
import com.azatdev.dailytasks.data.dao.data.GetBacklogByIdDaoImpl;
import com.azatdev.dailytasks.data.dao.data.GetTaskDaoImpl;
import com.azatdev.dailytasks.data.dao.data.MapActivitySessionToDomain;
import com.azatdev.dailytasks.data.dao.data.MapActivitySessionToDomainImpl;
import com.azatdev.dailytasks.data.dao.data.MapBacklogDataToDomain;
import com.azatdev.dailytasks.data.dao.data.MapNewActivitySessionToData;
import com.azatdev.dailytasks.data.dao.data.MapNewActivitySessionToDataImpl;
import com.azatdev.dailytasks.data.dao.data.MapTaskDataToDomain;
import com.azatdev.dailytasks.data.dao.data.MapTaskDataToDomainImpl;
import com.azatdev.dailytasks.data.dao.data.MarkTaskAsStoppedDaoImpl;
import com.azatdev.dailytasks.data.dao.data.StopActivitySessionDaoImpl;
import com.azatdev.dailytasks.data.dao.data.TasksRepositoryImpl;
import com.azatdev.dailytasks.data.dao.data.UpdateTaskStatusDaoImpl;
import com.azatdev.dailytasks.data.dao.data.transaction.TransactionImplFactory;
import com.azatdev.dailytasks.data.dao.data.user.MapBacklogDataToDomainImpl;
import com.azatdev.dailytasks.data.dao.persistence.GetRunningActivitySessionForTaskDaoImpl;
import com.azatdev.dailytasks.data.dao.persistence.backlog.BacklogRepositoryImpl;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaBacklogsRepository;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.dao.DeleteTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetBacklogByIdDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.MarkTaskAsStoppedDao;
import com.azatdev.dailytasks.domain.interfaces.dao.StopActivitySessionDao;
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
    public MapTaskDataToDomain mapTaskDataToDomain() {
        return new MapTaskDataToDomainImpl();
    }

    @Bean
    public TasksRepository tasksRepository(
        JpaUsersRepository jpaUsersRepository,
        JpaBacklogsRepository jpaBacklogsRepository,
        JpaTasksRepository jpaTasksRepository,
        MapTaskDataToDomain mapTaskDataToDomain
    ) {

        return new TasksRepositoryImpl(
            jpaUsersRepository::getReferenceById,
            jpaBacklogsRepository::getReferenceById,
            jpaTasksRepository,
            mapTaskDataToDomain
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

    @Bean
    public GetTaskDao getTaskDao(
        MapTaskDataToDomain mapTaskDataToDomain,
        JpaTasksRepository jpaTasksRepository
    ) {
        return new GetTaskDaoImpl(
            mapTaskDataToDomain,
            jpaTasksRepository
        );
    }

    @Bean
    public MarkTaskAsStoppedDao markTaskAsStoppedDao(JpaTasksRepository jpaTasksRepository) {
        return new MarkTaskAsStoppedDaoImpl(jpaTasksRepository);
    }

    @Bean
    public StopActivitySessionDao stopActivitySessionDao(JpaActivitySessionsRepository activitySessionsRepository) {
        return new StopActivitySessionDaoImpl(activitySessionsRepository);
    }

    @Bean
    public DeleteTaskDao deleteTaskDao(JpaTasksRepository jpaTasksRepository) {
        return new DeleteTaskDaoImpl(jpaTasksRepository);
    }

    @Bean
    public MapBacklogDataToDomain mapBacklogDataToDomain() {
        return new MapBacklogDataToDomainImpl();
    }

    @Bean
    public GetBacklogByIdDao getBacklogByIdDao(
        JpaBacklogsRepository jpaBacklogsRepository,
        MapBacklogDataToDomain mapBacklogDataToDomain
    ) {
        return new GetBacklogByIdDaoImpl(
            jpaBacklogsRepository,
            mapBacklogDataToDomain
        );
    }
}

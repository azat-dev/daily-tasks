package com.azatdev.dailytasks.presentation.config.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomainImpl;
import com.azatdev.dailytasks.data.repositories.data.TasksRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.data.transaction.TransactionImplFactory;
import com.azatdev.dailytasks.data.repositories.persistence.backlog.BacklogRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPABacklogRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPATasksRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;

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
        JPABacklogRepository jpaBacklogRepository
    ) {
        return new BacklogRepositoryImpl(
            jpaUsersRepository,
            jpaBacklogRepository
        );
    }

    @Bean
    public TasksRepository tasksRepository(JPATasksRepository jpaTasksRepository) {
        return new TasksRepositoryImpl(
            jpaTasksRepository,
            new MapTaskDataToDomainImpl()
        );
    }
}

package com.azatdev.dailytasks.data.repositories;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

@Component
public class TestEntityDataGenerator {

    // Fields
    @Autowired
    TestEntityManager entityManager;

    private final Faker faker = new Faker();

    // Methods

    public UserData anyUserDataWithUserName(String userName) {
        return new UserData(
            UUID.randomUUID(),
            userName,
            faker.internet()
                .password()
        );
    }

    public UserData givenExistingUser() {
        return anyUserDataWithUserName("anyUserName");
    }

    public UserData givenExistingUser(String userName) {
        return entityManager.persistAndFlush(anyUserDataWithUserName(userName));
    }

    public BacklogData anyWeekBacklog(UserData owner) {

        LocalDate wednesday = LocalDate.of(
            2021,
            1,
            6
        );
        return new BacklogData(
            owner,
            wednesday,
            BacklogData.Duration.WEEK
        );
    }

    public BacklogData givenExistingWeekBacklog(UserData owner) {
        return entityManager.persistAndFlush(anyWeekBacklog(owner));
    }

    public BacklogData givenExistingWeekBacklog(
        UserData owner,
        LocalDate starDate
    ) {
        return entityManager.persistAndFlush(

            new BacklogData(
                owner,
                starDate,
                BacklogData.Duration.WEEK
            )
        );
    }

    public BacklogData givenExistingDayBacklog(
        UserData owner,
        LocalDate starDate
    ) {
        return entityManager.persistAndFlush(

            new BacklogData(
                owner,
                starDate,
                BacklogData.Duration.DAY
            )
        );
    }

    public TaskData anyTaskData(
        UserData owner,
        BacklogData backlog,
        Integer orderInBacklog
    ) {
        return anyTaskData(
            owner,
            backlog,
            orderInBacklog,
            faker.lorem()
                .sentence()
        );
    }

    public TaskData anyTaskData(
        UserData owner,
        BacklogData backlog,
        Integer orderInBacklog,
        String title
    ) {
        return new TaskData(
            owner,
            backlog,
            orderInBacklog,
            title,
            faker.lorem()
                .paragraph(),
            TaskData.Status.NOT_STARTED,
            null
        );
    }
}

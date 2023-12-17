package com.azatdev.dailytasks.data.repositories;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.github.javafaker.Faker;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.ActivitySessionData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

public class TestEntityDataGenerator {

    // Fields
    @Autowired
    TestEntityManager entityManager;

    private final Faker faker = new Faker();

    // Methods

    private String limit(
        String text,
        int maxLength
    ) {
        return text.substring(
            0,
            Math.min(
                maxLength,
                text.length()
            )
        );
    }

    public UserData anyUserDataWithUserName(String userName) {
        return new UserData(
            UUID.randomUUID(),
            userName,
            limit(
                faker.internet()
                    .password(),
                255
            )
        );
    }

    public UserData givenExistingUser() {
        return givenExistingUser("anyUserName");
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

    public BacklogData givenExistingDayBacklog(UserData owner) {
        final var anyDay = LocalDate.of(
            2022,
            1,
            1
        );

        return entityManager.persistAndFlush(

            new BacklogData(
                owner,
                anyDay,
                BacklogData.Duration.DAY
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

    public TaskData givenExistingTaskData(
        UserData owner,
        BacklogData backlog,
        Integer orderInBacklog
    ) {
        return entityManager.persistAndFlush(
            anyTaskData(
                owner,
                backlog,
                orderInBacklog
            )
        );
    }

    public ActivitySessionData givenExistingActivitySession(
        UserData owner,
        TaskData task,
        ZonedDateTime startedAt,
        ZonedDateTime finishedAt
    ) {
        return entityManager.persistAndFlush(
            new ActivitySessionData(
                owner,
                task,
                startedAt,
                finishedAt
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
            limit(
                faker.lorem()
                    .sentence(),
                255
            )
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

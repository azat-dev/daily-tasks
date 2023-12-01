package com.azatdev.dailytasks.data.repositories;

import java.util.UUID;

import com.github.javafaker.Faker;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

public class TestEntityDataGenerator {

    // Fields

    private static final Faker faker = new Faker();

    // Methods

    public static TaskData anyTaskData(
        Long backlogId,
        Integer orderInBacklog
    ) {
        return anyTaskData(
            backlogId,
            orderInBacklog,
            faker.lorem()
                .sentence()
        );
    }

    public static TaskData anyTaskData(
        Long backlogId,
        Integer orderInBacklog,
        String title
    ) {
        return new TaskData(
            backlogId,
            orderInBacklog,
            title,
            faker.lorem()
                .paragraph(),
            TaskData.Status.NOT_STARTED,
            null
        );
    }

    public static UserData anyUserDataWithUserName(String userName) {
        return new UserData(
            UUID.randomUUID(),
            userName,
            faker.internet()
                .password()
        );
    }
}

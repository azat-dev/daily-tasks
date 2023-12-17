package com.azatdev.dailytasks.domain.usecases;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.github.javafaker.Faker;

import com.azatdev.dailytasks.domain.models.AppUser;
import com.azatdev.dailytasks.domain.models.Task;

public class TestDomainDataGenerator {

    // Fields

    private static final Faker faker = new Faker();

    // Methods

    private static String limit(
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

    public static Task anyTask(
        Long id,
        UUID ownerId
    ) {
        return new Task(
            id,
            ownerId,
            limit(
                faker.lorem()
                    .sentence(),
                255
            ),
            ZonedDateTime.now(),
            ZonedDateTime.now(),
            Task.Status.NOT_STARTED,
            Task.Priority.MEDIUM,
            faker.lorem()
                .paragraph()
        );
    }

    public static AppUser anyAppUserWithUserName(String username) {
        return new AppUser(
            UUID.randomUUID(),
            username,
            "password"
        );
    }
}

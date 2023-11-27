package com.azatdev.dailytasks.domain.usecases;

import com.github.javafaker.Faker;

import com.azatdev.dailytasks.domain.models.Task;

public class TestDomainDataGenerator {

    // Fields

    private static final Faker faker = new Faker();

    // Methods

    public static Task anyTask(Long id) {
        return new Task(
            id,
            faker.lorem()
                .sentence(),
            Task.Status.NOT_STARTED,
            Task.Priority.MEDIUM,
            faker.lorem()
                .paragraph()
        );
    }
}

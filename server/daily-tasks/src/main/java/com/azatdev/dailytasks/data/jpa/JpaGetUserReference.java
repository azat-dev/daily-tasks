package com.azatdev.dailytasks.data.jpa;

import java.util.UUID;

import com.azatdev.dailytasks.data.entities.UserData;

@FunctionalInterface
public interface JpaGetUserReference {
    UserData execute(UUID id);
}

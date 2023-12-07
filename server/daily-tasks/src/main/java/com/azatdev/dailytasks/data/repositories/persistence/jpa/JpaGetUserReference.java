package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import java.util.UUID;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;

@FunctionalInterface
public interface JpaGetUserReference {
    UserData execute(UUID id);
}

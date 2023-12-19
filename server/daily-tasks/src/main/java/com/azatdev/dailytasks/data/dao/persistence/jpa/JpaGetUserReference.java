package com.azatdev.dailytasks.data.dao.persistence.jpa;

import java.util.UUID;

import com.azatdev.dailytasks.data.dao.data.user.UserData;

@FunctionalInterface
public interface JpaGetUserReference {
    UserData execute(UUID id);
}

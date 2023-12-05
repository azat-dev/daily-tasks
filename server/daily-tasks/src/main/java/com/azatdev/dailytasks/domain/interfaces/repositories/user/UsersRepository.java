package com.azatdev.dailytasks.domain.interfaces.repositories.user;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.models.AppUser;

public interface UsersRepository extends UsersRepositoryCreate {

    // Methods

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findById(UUID id);
}

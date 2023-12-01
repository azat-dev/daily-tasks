package com.azatdev.dailytasks.domain.interfaces.repositories.user;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.models.AppUser;
import com.azatdev.dailytasks.utils.Result;

public interface UsersRepository {
    public enum Error {
        NOT_FOUND
    }

    public Result<Optional<AppUser>, Error> findByUsername(String username);

    public Result<Optional<AppUser>, Error> findById(UUID id);
}

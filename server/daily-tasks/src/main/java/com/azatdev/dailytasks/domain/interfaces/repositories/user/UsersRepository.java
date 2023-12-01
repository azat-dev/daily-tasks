package com.azatdev.dailytasks.domain.interfaces.repositories.user;

import java.util.Optional;

import com.azatdev.dailytasks.domain.models.User;
import com.azatdev.dailytasks.utils.Result;

public interface UsersRepository {
    public enum Error {
        NOT_FOUND
    }

    public Result<Optional<User>, Error> findByUsername(String username);
}

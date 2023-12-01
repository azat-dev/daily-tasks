package com.azatdev.dailytasks.domain.interfaces.repositories.user;

import java.util.Optional;

import com.azatdev.dailytasks.domain.models.AppUser;
import com.azatdev.dailytasks.utils.Result;

public interface UsersRepositoryFindByUserName {
    public enum Error {
        NOT_FOUND
    }

    public Result<Optional<AppUser>, Error> findByUsername(String username);
}

package com.azatdev.dailytasks.domain.interfaces.repositories.user;

import com.azatdev.dailytasks.domain.models.AppUser;

public interface UsersRepositoryCreate {

    // Methods

    public AppUser create(
        String username,
        String encodedPassword
    ) throws UsernameAlreadyExistsException;

    // Exceptions

    public class UsernameAlreadyExistsException extends Exception {
    }
}

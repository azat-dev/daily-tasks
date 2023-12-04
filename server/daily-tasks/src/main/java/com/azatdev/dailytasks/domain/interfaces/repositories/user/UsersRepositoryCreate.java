package com.azatdev.dailytasks.domain.interfaces.repositories.user;

import com.azatdev.dailytasks.domain.models.AppUser;

public interface UsersRepositoryCreate {

    // Methods

    public AppUser create(
        String username,
        String encodedPassword
    ) throws CreateException;

    // Exceptions

    public abstract class CreateException extends RuntimeException {
    }

    public class UsernameIsEmptyException extends CreateException {
    }

    public class UsernameAlreadyExistsException extends CreateException {
    }

    public class PasswordIsEmptyException extends CreateException {
    }

    public class InternalErrorException extends CreateException {
    }

}

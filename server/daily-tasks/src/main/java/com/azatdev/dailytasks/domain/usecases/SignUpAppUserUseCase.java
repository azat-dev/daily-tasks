package com.azatdev.dailytasks.domain.usecases;

import com.azatdev.dailytasks.domain.models.AppUser;

public interface SignUpAppUserUseCase {

    // Exceptions

    public abstract class UseCaseException extends RuntimeException {
    }

    public class UsernameAlreadyExistsException extends UseCaseException {
    }

    public class PasswordIsEmptyException extends UseCaseException {
    }

    public class InternalErrorException extends UseCaseException {
    }

    // Methods

    public AppUser execute(
        String username,
        String encodedPassword
    ) throws UseCaseException;
}

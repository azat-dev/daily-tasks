package com.azatdev.dailytasks.domain.usecases;

import com.azatdev.dailytasks.domain.models.AppUser;

public interface SignUpAppUserUseCase {

    // Methods

    public AppUser execute(
        String username,
        String encodedPassword
    ) throws UsernameAlreadyExistsException, PasswordIsEmptyException, UsernameIsEmptyException;

    // Exceptions

    public class UsernameAlreadyExistsException extends Exception {
    }

    public class PasswordIsEmptyException extends Exception {
    }

    public class UsernameIsEmptyException extends Exception {
    }
}

package com.azatdev.dailytasks.domain.usecases;

import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryCreate;
import com.azatdev.dailytasks.domain.models.AppUser;

public final class SignUpAppUserUseCaseImpl implements SignUpAppUserUseCase {

    private final UsersRepositoryCreate usersRepository;

    public SignUpAppUserUseCaseImpl(UsersRepositoryCreate usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public AppUser execute(
        String username,
        String encodedPassword
    ) throws UsernameAlreadyExistsException, PasswordIsEmptyException, UsernameIsEmptyException {

        final var cleanedUserName = username.toLowerCase()
            .trim();

        if (username.isEmpty()) {
            throw new UsernameIsEmptyException();
        }

        if (encodedPassword.isEmpty()) {
            throw new PasswordIsEmptyException();
        }

        try {
            return usersRepository.create(
                cleanedUserName,
                encodedPassword
            );
        } catch (UsersRepositoryCreate.UsernameAlreadyExistsException e) {
            throw new UsernameAlreadyExistsException();
        }
    }
}

package com.azatdev.dailytasks.domain.usecases;

import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryCreate;
import com.azatdev.dailytasks.domain.models.AppUser;

public final class SignUpAppUserUseCaseImpl implements SignUpAppUserUseCase {

    // Constructors

    private final UsersRepositoryCreate usersRepository;

    public SignUpAppUserUseCaseImpl(UsersRepositoryCreate usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public AppUser execute(
        String username,
        String encodedPassword
    ) throws UseCaseException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}

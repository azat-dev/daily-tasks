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
    ) throws UseCaseException {

        try {
            return usersRepository.create(
                username,
                encodedPassword
            );
        } catch (UsersRepositoryCreate.UsernameAlreadyExistsException e) {
            throw new UsernameAlreadyExistsException();
        } catch (UsersRepositoryCreate.PasswordIsEmptyException e) {
            throw new PasswordIsEmptyException();
        } catch (UsersRepositoryCreate.InternalErrorException e) {
            throw new InternalErrorException();
        }
    }
}

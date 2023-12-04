package com.azatdev.dailytasks.data.repositories.data;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.domain.models.AppUser;
import com.azatdev.dailytasks.utils.Result;

public class UsersRepositoryImpl implements UsersRepository {

    private final JpaUsersRepository jpaUsersRepository;

    public UsersRepositoryImpl(JpaUsersRepository jpaUsersRepository) {
        this.jpaUsersRepository = jpaUsersRepository;
    }

    private AppUser mapUserDataToAppUser(UserData userData) {
        return new AppUser(
            userData.id(),
            userData.username(),
            userData.password()
        );
    }

    @Override
    public Result<Optional<AppUser>, UsersRepository.Error> findByUsername(String username) {
        final var foundUserDataResult = jpaUsersRepository.findByUsername(username);

        if (foundUserDataResult.isEmpty()) {
            return Result.success(Optional.empty());
        }

        final var foundUserData = foundUserDataResult.get();
        return Result.success(Optional.of(mapUserDataToAppUser(foundUserData)));
    }

    @Override
    public Result<Optional<AppUser>, Error> findById(UUID userId) {
        final var foundUserDataResult = jpaUsersRepository.findById(userId);

        if (foundUserDataResult.isEmpty()) {
            return Result.success(Optional.empty());
        }

        final var foundUserData = foundUserDataResult.get();
        return Result.success(Optional.of(mapUserDataToAppUser(foundUserData)));
    }

    @Override
    public AppUser create(
        String username,
        String encodedPassword
    ) throws CreateException {

        if (username == null || username.isEmpty()) {
            throw new UsernameIsEmptyException();
        }

        if (encodedPassword == null || encodedPassword.isEmpty()) {
            throw new PasswordIsEmptyException();
        }

        final var foundUserDataResult = jpaUsersRepository.findByUsername(username);

        if (foundUserDataResult.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        final var userData = new UserData(
            UUID.randomUUID(),
            username,
            encodedPassword
        );

        final var createdUserData = jpaUsersRepository.saveAndFlush(userData);

        return mapUserDataToAppUser(createdUserData);
    }
}

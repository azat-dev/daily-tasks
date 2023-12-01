package com.azatdev.dailytasks.data.repositories.data;

import java.util.Optional;

import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryFindByUserName;
import com.azatdev.dailytasks.domain.models.AppUser;
import com.azatdev.dailytasks.utils.Result;

public class UsersRepositoryImpl implements UsersRepositoryFindByUserName {

    private final JpaUsersRepository jpaUsersRepository;

    public UsersRepositoryImpl(JpaUsersRepository jpaUsersRepository) {
        this.jpaUsersRepository = jpaUsersRepository;
    }

    @Override
    public Result<Optional<AppUser>, UsersRepositoryFindByUserName.Error> findByUsername(String username) {
        final var foundUserDataResult = jpaUsersRepository.findByUsername(username);

        if (foundUserDataResult.isEmpty()) {
            return Result.success(Optional.empty());
        }

        final var foundUserData = foundUserDataResult.get();

        final var user = new AppUser(
            foundUserData.id(),
            foundUserData.username(),
            foundUserData.password()
        );

        return Result.success(Optional.of(user));
    }
}

package com.azatdev.dailytasks.data.dao.data;

import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;

import com.azatdev.dailytasks.data.dao.data.user.UserData;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.domain.models.AppUser;

public final class UsersRepositoryImpl implements UsersRepository {

    private final JpaUsersRepository jpaUsersRepository;

    public UsersRepositoryImpl(JpaUsersRepository jpaUsersRepository) {
        this.jpaUsersRepository = jpaUsersRepository;
    }

    private AppUser mapUserDataToAppUser(UserData userData) {
        return new AppUser(
            userData.getId(),
            userData.getUsername(),
            userData.getPassword()
        );
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {

        final var foundUserDataResult = jpaUsersRepository.findByUsername(username);

        return foundUserDataResult.map((userData) -> mapUserDataToAppUser(userData));
    }

    @Override
    public Optional<AppUser> findById(UUID userId) {

        final var foundUserDataResult = jpaUsersRepository.findById(userId);

        return foundUserDataResult.map((userData) -> mapUserDataToAppUser(userData));
    }

    @Override
    public AppUser create(
        String username,
        String encodedPassword
    ) throws UsernameAlreadyExistsException {

        final var foundUserDataResult = jpaUsersRepository.findByUsername(username);

        if (foundUserDataResult.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        final var userData = new UserData(
            UUID.randomUUID(),
            username,
            encodedPassword
        );

        try {
            final var createdUserData = jpaUsersRepository.saveAndFlush(userData);
            return mapUserDataToAppUser(createdUserData);
        } catch (DataIntegrityViolationException e) {

            final var isUserWithSameUsernameExists = jpaUsersRepository.findByUsername(username)
                .isPresent();

            if (isUserWithSameUsernameExists) {
                throw new UsernameAlreadyExistsException();
            }

            throw new RuntimeException(e);
        }
    }
}

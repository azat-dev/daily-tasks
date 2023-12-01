package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryFindByUserName;
import com.azatdev.dailytasks.domain.models.AppUser;
import com.azatdev.dailytasks.utils.Result;

interface JpaUsersRepository extends JpaRepository<UserData, UUID> {

    Optional<UserData> findByUsername(String username);
}

class UsersRepositoryImpl implements UsersRepositoryFindByUserName {

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

class UsersRepositoryImplTests {

    private record SUT(
        UsersRepositoryFindByUserName usersRepository,
        JpaUsersRepository jpaUsersRepository
    ) {
    }

    private SUT createSUT() {
        JpaUsersRepository jpaUsersRepository = mock(JpaUsersRepository.class);
        UsersRepositoryFindByUserName usersRepository = new UsersRepositoryImpl(jpaUsersRepository);

        return new SUT(
            usersRepository,
            jpaUsersRepository
        );
    }

    @Test
    void findByUsernameNotExistingUserMustReturnEmptyOptionalTest() {

        // Given
        String wrongUserName = "wrongUserName";

        SUT sut = createSUT();
        given(sut.jpaUsersRepository.findByUsername(wrongUserName)).willReturn(Optional.empty());

        // When
        final var result = sut.usersRepository.findByUsername(wrongUserName);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findByUsername(wrongUserName);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEmpty();
    }

    @Test
    void findByUsernameExistingUserMustReturnUserTest() {

        // Given
        final String userName = "userName";
        final var userData = TestEntityDataGenerator.anyUserDataWithUserName(userName);

        SUT sut = createSUT();
        given(sut.jpaUsersRepository.findByUsername(userName)).willReturn(Optional.of(userData));

        // When
        final var result = sut.usersRepository.findByUsername(userName);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findByUsername(userName);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isPresent();

        final var expectedUser = new AppUser(
            userData.id(),
            userData.username(),
            userData.password()
        );

        assertThat(
            result.getValue()
                .get()
        ).isEqualTo(expectedUser);
    }
}

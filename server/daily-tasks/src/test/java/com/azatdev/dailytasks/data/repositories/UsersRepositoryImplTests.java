package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.data.repositories.data.UsersRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.domain.models.AppUser;

class UsersRepositoryImplTests {

    private record SUT(
        UsersRepository usersRepository,
        JpaUsersRepository jpaUsersRepository
    ) {
    }

    private SUT createSUT() {
        JpaUsersRepository jpaUsersRepository = mock(JpaUsersRepository.class);
        UsersRepository usersRepository = new UsersRepositoryImpl(jpaUsersRepository);

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

    @Test
    void findByIdNotExistingUserMustReturnEmptyOptionalTest() {

        // Given
        final var wrongUserId = UUID.randomUUID();

        SUT sut = createSUT();
        given(sut.jpaUsersRepository.findById(wrongUserId)).willReturn(Optional.empty());

        // When
        final var result = sut.usersRepository.findById(wrongUserId);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findById(wrongUserId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEmpty();
    }

    @Test
    void findByIdExistingUserMustReturnUserTest() {

        // Given
        final var userData = TestEntityDataGenerator.anyUserDataWithUserName("userName");
        final var userId = userData.id();

        SUT sut = createSUT();
        given(sut.jpaUsersRepository.findById(userId)).willReturn(Optional.of(userData));

        // When
        final var result = sut.usersRepository.findById(userId);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findById(userId);

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

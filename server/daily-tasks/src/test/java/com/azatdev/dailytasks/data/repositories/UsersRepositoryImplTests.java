package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.azatdev.dailytasks.data.repositories.data.UsersRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.domain.models.AppUser;

@ExtendWith(SpringExtension.class)
class UsersRepositoryImplTest {

    private TestEntityDataGenerator testData;

    @BeforeEach
    void setupTestDataManager() {
        testData = new TestEntityDataGenerator();
    }

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
    void findByUsername_givenNotExistingUser_thenMustReturnEmptyOptional() {

        // Given
        String wrongUserName = "wrongUserName";

        SUT sut = createSUT();
        given(sut.jpaUsersRepository.findByUsername(wrongUserName)).willReturn(Optional.empty());

        // When
        final var result = sut.usersRepository.findByUsername(wrongUserName);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findByUsername(wrongUserName);

        assertThat(result).isEmpty();
    }

    @Test
    void findByUsername_givenExistingUser_thenMustReturnUser() {

        // Given
        final String userName = "userName";
        final var userData = testData.anyUserDataWithUserName(userName);

        SUT sut = createSUT();
        given(sut.jpaUsersRepository.findByUsername(userName)).willReturn(Optional.of(userData));

        // When
        final var result = sut.usersRepository.findByUsername(userName);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findByUsername(userName);

        assertThat(result).isPresent();

        final var expectedUser = new AppUser(
            userData.getId(),
            userData.getUsername(),
            userData.getPassword()
        );

        assertThat(result.get()).isEqualTo(expectedUser);
    }

    @Test
    void findById_givenNotExistingUser_thenMustReturnEmptyOptional() {

        // Given
        final var wrongUserId = UUID.randomUUID();

        SUT sut = createSUT();
        given(sut.jpaUsersRepository.findById(wrongUserId)).willReturn(Optional.empty());

        // When
        final var result = sut.usersRepository.findById(wrongUserId);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findById(wrongUserId);

        assertThat(result).isEmpty();
    }

    @Test
    void findById_givenExistingUser_thenMustReturnUser() {

        // Given
        final var owner = testData.anyUserDataWithUserName("userName");
        final var userId = owner.getId();

        SUT sut = createSUT();
        given(sut.jpaUsersRepository.findById(userId)).willReturn(Optional.of(owner));

        // When
        final var result = sut.usersRepository.findById(userId);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findById(userId);

        assertThat(result).isPresent();

        final var expectedUser = new AppUser(
            owner.getId(),
            owner.getUsername(),
            owner.getPassword()
        );

        assertThat(result.get()).isEqualTo(expectedUser);
    }

    @Test
    void create_givenValidData_thenMustCreateUser() throws Exception {

        // Given
        final var username = "username";
        final var encodedPassword = "encodedPassword";

        final var sut = createSUT();

        final var userId = UUID.randomUUID();

        final var expectedAppUser = new AppUser(
            userId,
            username,
            encodedPassword
        );

        final var expectedUserData = new UserData(
            userId,
            username,
            encodedPassword
        );

        given(sut.jpaUsersRepository.saveAndFlush(any())).willReturn(expectedUserData);

        // When
        final var createdUser = sut.usersRepository.create(
            username,
            encodedPassword
        );

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .saveAndFlush(any());

        assertThat(createdUser).isEqualTo(expectedAppUser);
    }
}

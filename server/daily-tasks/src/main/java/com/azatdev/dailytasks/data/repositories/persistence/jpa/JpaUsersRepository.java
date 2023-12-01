package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;

public interface JpaUsersRepository extends JpaRepository<UserData, UUID> {

    public Optional<UserData> findByUsername(String username);

    public Optional<UserData> findById(UUID id);
}
